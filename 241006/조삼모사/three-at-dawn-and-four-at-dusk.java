import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	
	static int N, ans = Integer.MAX_VALUE;
	static int[][] work;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		N = Integer.parseInt(br.readLine());
		work = new int[N][N];
		int total = 0;
		
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				work[i][j] = Integer.parseInt(st.nextToken());
				total += work[i][j];
			}
		}
		for (int i = 1; i < N; i++) {
			total -= (work[0][i] + work[i][0]);
		}

		combi(1, 1, 0, total, 1);
		System.out.println(ans);
	}
	static void combi(int depth, int start, int morning, int evening, int flag) {
		if (depth == N/2) {
			ans = Math.min(ans, Math.abs(morning-evening));
			return;
		}
		for (int i = start; i < N; i++) {
			int[] w = calWork(i, flag);
			combi(depth+1, i+1, morning + w[0], evening - w[1], flag|1<<i);
		}
	}
	static int[] calWork(int idx, int flag) {
		int[] res = new int[2];
		for (int i = 0; i < N; i++) {
			if ((flag&1<<i) != 0) res[0] += work[i][idx] + work[idx][i];
			else res[1] += work[i][idx] + work[idx][i];
		}
		return res;
	}
}