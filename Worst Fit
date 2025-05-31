public class BinPacking {
    public static int worstFit(int[] size, int C) {
        PriorityQueue<Integer> pq = new
                PriorityQueue<> (Collections.reverseOrder());
        for (int item : size) {
            if (!pq.isEmpty()) {
                int mostSpace = pq.poll();
                if (mostSpace >= item) {
                    pq.offer(mostSpace - item);
                } else {
                    pq.offer(mostSpace);
                    pq.offer(C - item);
                }
            } else {
                pq.offer(C - item);
            }
        }
        return pq.size();
    }
}
