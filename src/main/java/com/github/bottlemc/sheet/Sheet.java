package com.github.bottlemc.sheet;

import com.electronwill.toml.Toml;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Sheet {

    private final File PARENT_CONFIG_FILE = new File("sheet");

    private final Map<String, Class<?>> typeMap = new HashMap<>();

    private final Map<String, Object> configurations = new HashMap<>();

    public void registerType(String id, Class<?> type) {
        this.typeMap.put(id, type);
    }

    public void loadFromFiles() {
        PARENT_CONFIG_FILE.mkdirs();

        try {
            for (File file : Objects.requireNonNull(PARENT_CONFIG_FILE.listFiles())) {
                if (file.getName().endsWith(".toml")) {
                    Map<String, Object> toml = Toml.read(file);
                    String id = file.getName().replace(".toml", "");
                    this.configurations.put(id, this.deserialize(id, toml));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToFiles() {
        PARENT_CONFIG_FILE.mkdirs();

        try {
            for(Map.Entry<String, Object> configuration : this.configurations.entrySet()) {
                File file = new File(PARENT_CONFIG_FILE, configuration.getKey() + ".toml");
                Toml.write(this.serialize(configuration.getValue()), file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object deserialize(String id, Map<String, Object> toml) {
        try {
            return this.deserializeInternal(toml, this.typeMap.get(id).getConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private Object deserializeInternal(Map<String, Object> toml, Object typeObject) {
        for (Map.Entry<String, Object> entry : toml.entrySet()) {
            Field field;
            try {
                field = typeObject.getClass().getDeclaredField(entry.getKey());
            } catch (NoSuchFieldException e) {
                field = null;
            }
            if(field != null) {
                field.setAccessible(true);

                Object value = entry.getValue();

                try {
                    if(value.getClass().getPackage().getName().equals("java.lang")) {
                        field.set(typeObject, entry.getValue());
                    } else {
                        Constructor<?> constructor = field.getType().getDeclaredConstructors()[0];

                        Object[] args = new Object[constructor.getParameterCount()];
                        Object newInstance = constructor.newInstance(args);
                        field.set(typeObject, this.deserializeInternal((Map<String, Object>) entry.getValue(), newInstance));
                    }
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        return typeObject;
    }

    private Map<String, Object> serialize(Object typeObject) {
        Map<String, Object> tomlMap = new HashMap<>();

        for (Field field : typeObject.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            if (field.isSynthetic()) {
                continue;
            }
            try {
                Object object = field.get(typeObject);
                if (object != null) {
                    if (object.getClass().getPackage().getName().equals("java.lang")) {
                        tomlMap.put(field.getName(), object);
                    } else {
                        tomlMap.put(field.getName(), this.serialize(object));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return tomlMap;
    }

    @SuppressWarnings("unchecked")
    public <T> T load(String id) {
        return (T) this.configurations.computeIfAbsent(id, k -> {
            try {
                return this.typeMap.get(id).getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public <T> void save(String id, T object) {
        this.configurations.put(id, object);
    }

}
