package com.example.prm392pe.utils;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for collection operations
 */
public class CollectionUtils {
    
    /**
     * Check if collection is null or empty
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
    
    /**
     * Check if collection is not null and not empty
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
    
    /**
     * Get safe size of collection (returns 0 if null)
     */
    public static int safeSize(Collection<?> collection) {
        return collection != null ? collection.size() : 0;
    }
    
    /**
     * Convert array to list
     */
    public static <T> List<T> arrayToList(T[] array) {
        List<T> list = new ArrayList<>();
        if (array != null) {
            for (T item : array) {
                list.add(item);
            }
        }
        return list;
    }
    
    /**
     * Filter list by predicate
     */
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        List<T> filtered = new ArrayList<>();
        if (list != null && predicate != null) {
            for (T item : list) {
                if (predicate.test(item)) {
                    filtered.add(item);
                }
            }
        }
        return filtered;
    }
    
    /**
     * Find first item matching predicate
     */
    public static <T> T findFirst(List<T> list, Predicate<T> predicate) {
        if (list != null && predicate != null) {
            for (T item : list) {
                if (predicate.test(item)) {
                    return item;
                }
            }
        }
        return null;
    }
    
    /**
     * Check if any item matches predicate
     */
    public static <T> boolean anyMatch(List<T> list, Predicate<T> predicate) {
        return findFirst(list, predicate) != null;
    }
    
    /**
     * Transform list using mapper function
     */
    public static <T, R> List<R> map(List<T> list, Mapper<T, R> mapper) {
        List<R> mapped = new ArrayList<>();
        if (list != null && mapper != null) {
            for (T item : list) {
                mapped.add(mapper.map(item));
            }
        }
        return mapped;
    }
    
    /**
     * Join list elements to string
     */
    public static <T> String join(List<T> list, String separator) {
        if (isEmpty(list)) return "";
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(separator);
            sb.append(list.get(i).toString());
        }
        return sb.toString();
    }
    
    /**
     * Create map from lists of keys and values
     */
    public static <K, V> Map<K, V> zipToMap(List<K> keys, List<V> values) {
        Map<K, V> map = new HashMap<>();
        if (keys != null && values != null) {
            int size = Math.min(keys.size(), values.size());
            for (int i = 0; i < size; i++) {
                map.put(keys.get(i), values.get(i));
            }
        }
        return map;
    }
    
    /**
     * Get item at index safely (returns null if out of bounds)
     */
    public static <T> T safeGet(List<T> list, int index) {
        if (list != null && index >= 0 && index < list.size()) {
            return list.get(index);
        }
        return null;
    }
    
    /**
     * Get item at index with default value
     */
    public static <T> T safeGet(List<T> list, int index, T defaultValue) {
        T item = safeGet(list, index);
        return item != null ? item : defaultValue;
    }
    
    /**
     * Functional interface for filtering
     */
    public interface Predicate<T> {
        boolean test(T item);
    }
    
    /**
     * Functional interface for mapping
     */
    public interface Mapper<T, R> {
        R map(T item);
    }
}
