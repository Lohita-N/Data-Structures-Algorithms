package lxn240002;

import java.util.*;

public class MDS {
    // Maps item ID to Item object
    private Map<Integer, Item> idMap = new HashMap<>();
    // Maps description number to a sorted set of items by price
    private Map<Integer, TreeSet<Item>> descMap = new HashMap<>();

    // Represents an item
    private class Item implements Comparable<Item> {
        int id;
        int price;
        List<Integer> description;

        // Constructor
        Item(int id, int price, List<Integer> description) {
            this.id = id;
            this.price = price;
            this.description = new ArrayList<>(description);
        }

        // Compare items by price, then by id
        @Override
        public int compareTo(Item other) {
            if (this.price != other.price) {
                return Integer.compare(this.price, other.price);
            }
            return Integer.compare(this.id, other.id);
        }

        // Items are equal if they have the same id
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Item)) return false;
            Item other = (Item) obj;
            return this.id == other.id;
        }

        // Hash code based on id
        @Override
        public int hashCode() {
            return Integer.hashCode(id);
        }
    }

    // a. Insert a new item or update an existing one
    public int insert(int id, int price, List<Integer> list) {
        boolean isNew = !idMap.containsKey(id);

        if (isNew) {
            // Add new item
            Item item = new Item(id, price, list);
            idMap.put(id, item);
            for (int desc : list) {
                descMap.computeIfAbsent(desc, k -> new TreeSet<>()).add(item);
            }
            return 1;
        } else {
            // Update existing item
            Item oldItem = idMap.get(id);
            if (list == null || list.isEmpty()) {
                // Update price only
                for (int desc : oldItem.description) {
                    TreeSet<Item> set = descMap.get(desc);
                    set.remove(oldItem);
                }
                oldItem.price = price;
                for (int desc : oldItem.description) {
                    descMap.get(desc).add(oldItem);
                }
                return 0;
            } else {
                // Update price and description
                for (int desc : oldItem.description) {
                    descMap.get(desc).remove(oldItem);
                }
                oldItem.price = price;
                oldItem.description = new ArrayList<>(list);
                for (int desc : list) {
                    descMap.computeIfAbsent(desc, k -> new TreeSet<>()).add(oldItem);
                }
                return 0;
            }
        }
    }

    // b. Return price of item by id
    public int find(int id) {
        Item item = idMap.get(id);
        return item == null ? 0 : item.price;
    }

    // c. Delete item by id and return sum of its descriptions
    public int delete(int id) {
        Item item = idMap.remove(id);
        if (item == null) return 0;

        int sum = 0;
        for (int desc : item.description) {
            sum += desc;
            TreeSet<Item> set = descMap.get(desc);
            set.remove(item);
            if (set.isEmpty()) {
                descMap.remove(desc);
            }
        }
        return sum;
    }

    // d. Return minimum price for items with description n
    public int findMinPrice(int n) {
        TreeSet<Item> set = descMap.get(n);
        if (set == null || set.isEmpty()) return 0;
        return set.first().price;
    }

    // e. Return maximum price for items with description n
    public int findMaxPrice(int n) {
        TreeSet<Item> set = descMap.get(n);
        if (set == null || set.isEmpty()) return 0;
        return set.last().price;
    }

    // f. Count how many items have price in range [low, high] with description n
    public int findPriceRange(int n, int low, int high) {
        TreeSet<Item> set = descMap.get(n);
        if (set == null || set.isEmpty()) return 0;

        int count = 0;
        for (Item item : set) {
            if (item.price >= low && item.price <= high) {
                count++;
            } else if (item.price > high) {
                break;
            }
        }
        return count;
    }

    // g. Remove given descriptions from item and return their sum
    public int removeNames(int id, List<Integer> list) {
        Item item = idMap.get(id);
        if (item == null) return 0;

        int sum = 0;
        for (int desc : list) {
            if (item.description.contains(desc)) {
                item.description.removeAll(Collections.singleton(desc));
                sum += desc;
                TreeSet<Item> set = descMap.get(desc);
                if (set != null) {
                    set.remove(item);
                    if (set.isEmpty()) {
                        descMap.remove(desc);
                    }
                }
            }
        }

        // Re-insert into updated description sets
        for (int desc : item.description) {
            descMap.computeIfAbsent(desc, k -> new TreeSet<>()).add(item);
        }

        return sum;
    }
}
