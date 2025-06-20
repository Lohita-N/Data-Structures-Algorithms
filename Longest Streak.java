public static int longestStreak(int[] A) {
    HashSet<Integer> set = new HashSet<>( );
    for (int num : A) {
        set.add(num);
    }
    int longest = 0;
    for (int num : A) {
        if (!set.contains(num - 1)) {
            int current = num;
            int streak = 1;
            while (set.contains(current + 1)) {
                current++;
                streak++;
            }
            longest = Math.max(longest, streak);
        }
    }
    return longest;
}
