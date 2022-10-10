package coding.silver;

import java.io.*;
import java.util.*;

public class _1260_DFS_BFS_LIST {
    static int N;
    static int M;
    static boolean[] visited;
    static List<List<Integer>> graph = new ArrayList<>();
    static StringBuilder dfs_sb = new StringBuilder();
    static StringBuilder bfs_sb = new StringBuilder();

    // Silver 2 - DFS와 BFS, 인접 리스트
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String[] S = br.readLine().split(" ");
        N = Integer.parseInt(S[0]);
        M = Integer.parseInt(S[1]);
        int V = Integer.parseInt(S[2]);

        for (int i=0; i<=N; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i=0; i<M; i++) {
            String[] lines = br.readLine().split(" ");
            int x = Integer.parseInt(lines[0]);
            int y = Integer.parseInt(lines[1]);
            graph.get(x).add(y);
            graph.get(y).add(x);
        }

        for (int i=1; i<=N; i++) {
            Collections.sort(graph.get(i));
        }

        visited = new boolean[N+1];
        dfs(V);
        visited = new boolean[N+1];
        bfs(V);

        System.out.println(dfs_sb);
        System.out.println(bfs_sb);
    }

    public static void dfs(int V) {
        dfs_sb.append(V).append(" ");
        visited[V] = true;
        for (int v : graph.get(V)) {
            if (!visited[v]) dfs(v);
        }
    }

    public static void bfs(int V) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(V);
        bfs_sb.append(V).append(" ");
        visited[V] = true;

        while (!queue.isEmpty()) {
            for (int v : graph.get(queue.poll())) {
                if (!visited[v]) {
                    queue.offer(v);
                    bfs_sb.append(v).append(" ");
                    visited[v] = true;
                }
            }
        }
    }
}
