import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy {


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        List<Point> path = new LinkedList<>();
        PriorityQueue<Node> openList = new PriorityQueue<>();
        HashMap<Point, Node> closedList = new HashMap<>();
        boolean found = false;
        Node lastNode = null;


        openList.offer(new Node(start, -1, end, null));

        while (openList.size() > 0 && !found) {

            Node chk = openList.poll();
            closedList.put(chk.pos, chk);

            for (Point ip : potentialNeighbors.apply(chk.pos)
                    .filter(canPassThrough)
                    .filter(p -> !closedList.containsKey(p))
                    .collect(Collectors.toList())) {
                Node tmp = new Node(ip, chk.getG(), end, chk);

                if (withinReach.test(ip, end)) {
                    found = true;
                    lastNode = tmp;
                }
                if(openList.contains(tmp)){
                    openList.remove(tmp);
                    openList.offer(tmp);
                }else {
                    openList.offer(tmp);
                }
            }
        }
        if (found) {
            Node n = lastNode;
            while (!n.pos.equals(start)) {
                path.add(n.pos);
                n = n.prv;
            }
        }
        Collections.reverse(path);
        return path;
    }

    private class Node implements Comparable {
        public Point pos, dst;
        public Node prv;
        private int g;

        public Node(Point pos, int prevG, Point dst, Node prv) {
            this.pos = pos;
            this.g = prevG + 1;
            this.dst = dst;
            this.prv = prv;
        }

        @Override
        public int compareTo(Object o) {
            if (o != null && o instanceof Node) {
                return this.getF() - ((Node) o).getF();
            }
            return 0;
        }

        public int getH() {
            return Math.abs(pos.x - dst.x) + Math.abs(pos.y - dst.y);
        }

        public int getG() {
            return g;
        }

        public int getF() {
            return getH() + getG();
        }

        @Override
        public String toString() {
            return "Node{" +
                    "pos=" + pos +
                    ", g=" + g +
                    ", h=" + getH() +
                    ", f=" + getF() +
                    ", dst=" + dst +
                    ", prv=" + prv +
                    "}\n";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            return pos != null ? pos.equals(node.pos) : node.pos == null;

        }

        @Override
        public int hashCode() {
            return pos != null ? pos.hashCode() : 0;
        }
    }
}
