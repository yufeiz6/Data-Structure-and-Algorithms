package graphs.minspantrees;

import disjointsets.DisjointSets;
import disjointsets.QuickFindDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        return new QuickFindDisjointSets<>();
        /*
        Disable the line above and enable the one below after you've finished implementing
        your `UnionBySizeCompressingDisjointSets`.
         */
        // return new UnionBySizeCompressingDisjointSets<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        // Here's some code to get you started; feel free to change or rearrange it if you'd like.

        // sort edges in the graph in ascending weight order
        List<E> edges = new ArrayList<>(graph.allEdges());
        edges.sort(Comparator.comparingDouble(E::weight));

        DisjointSets<V> disjointSets = createDisjointSets();
        for (V vertex: graph.allVertices()) {
            disjointSets.makeSet(vertex);
        }
        List<E> result = new ArrayList<E>();

        for (E edge : edges) {
            V vertex1 = edge.from();
            V vertex2 = edge.to();
            if (!vertex1.equals(vertex2)) {
                if (disjointSets.findSet(vertex1) != disjointSets.findSet(vertex2)) {
                    disjointSets.union(vertex1, vertex2);
                    result.add(edge);
                }
            }
        }
        Set<Integer> idSet = new TreeSet<>();
        for (V vertex : graph.allVertices()) {
            idSet.add(disjointSets.findSet(vertex));
        }
        if (idSet.size() > 1) {
            return new MinimumSpanningTree.Failure<>();
        }

        return new MinimumSpanningTree.Success<>(result);
    }
}
