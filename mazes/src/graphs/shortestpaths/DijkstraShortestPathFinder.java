package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {

        HashMap<V, E> paths = new HashMap<>();
        if (Objects.equals(start, end)) {
            return paths;
        }
        HashMap<V, Double> distTo = new HashMap<>();

        distTo.put(start, 0.0);
        DoubleMapMinPQ<V> peri = new DoubleMapMinPQ<>();
        peri.add(start, 0);
        while (!peri.isEmpty()) {
            V prev = peri.removeMin();
            for (E edge : graph.outgoingEdgesFrom(prev)) {
                V reach = edge.to();
                double oldDist = distTo.getOrDefault(edge.to(), Double.POSITIVE_INFINITY);
                double newDist = distTo.get(prev) + edge.weight();
                if (!(distTo.containsKey(end) && distTo.get(end) <= newDist) && newDist < oldDist) {
                    distTo.put(edge.to(), newDist);
                    paths.put(reach, edge);
                    if (peri.contains(reach)) {
                        peri.changePriority(reach, newDist);
                    } else {
                        peri.add(reach, newDist);
                    }
                }
            }
        }
        return paths;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }

        E edge = spt.get(end);
        if (edge == null) {
            return new ShortestPath.Failure<>();
        }

        List<E> paths = new ArrayList<>();
        V current = end;
        while (!Objects.equals(start, current)) {
            paths.add(spt.get(current));
            current = spt.get(current).from();
        }
        List<E> result = new ArrayList<>();
        for (int i = paths.size() - 1; i >= 0; i--) {
            result.add(paths.get(i));
        }
        return new ShortestPath.Success<>(result);
    }
}
