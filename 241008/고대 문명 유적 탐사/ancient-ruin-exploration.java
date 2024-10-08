import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	
	static int K, pointer, ans;
	static boolean pos = true;
	static int[] wall;
	static int[][] site;
	static boolean[][] visited;
	static int[] dy = {-1,1,0,0}, dx = {0,0,-1,1}; 
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		K = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		site = new int[5][5];
		for (int i = 0; i < 5; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < 5; j++) {
				site[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		wall = new int[M];
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < M; i++) {
			wall[i] = Integer.parseInt(st.nextToken());
		}

		for (int i = 0; i < K; i++) {
			explore();
			if (!pos) break;
			
			ans = 0;
			while (true) {
				int value = getRelic();
				if (value == 0) break;
				fillRelic();
				ans += value;
			}
			System.out.print(ans + " ");
		}
	}
	
	static void explore() {								// (1)탐사 진행
		int maxCount = 0, rotateCnt = 0;
		Point location = null;
		for (int i = 0; i < 3; i++) {	// y, x 0-2까지
			for (int j = 0; j < 3; j++) {
				for (int k = 1; k < 4; k++) {
					rotate(j, i);
					int value = check();
					if (maxCount < value || (maxCount == value && k < rotateCnt)) {
						maxCount = value;
						rotateCnt = k;
						location = new Point(i, j);
					}
				}
				rotate(j, i);
			}
		}
		if (rotateCnt == 0) {
			pos = false;
			return;
		}
		
		for (int i = 0; i < rotateCnt; i++) {
			rotate(location.y, location.x);
		}
	}
	
	static int getRelic() {								// (2)유물 획득
		visited = new boolean[5][5];
		int total = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (!visited[i][j]) {
					total += deleteRelic(i,j);
				}
			}
		}
		return total;
	}

	static void rotate(int y, int x) {					// 3x3 회전 
		int[] numbers = new int[9];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				numbers[i*3+j] = site[y+i][x+j];
			}
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				site[y+j][x+2-i] = numbers[i*3+j];
			}
		}
	}
	
	static int check() {								// 현재 유물 가치 체크
		visited = new boolean[5][5];
		int total = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (!visited[i][j]) {
					total += bfs(i, j);
				}
			}
		}
		return total;
	}
	
	static int bfs(int y, int x) {						// 유물 개수 체크
		Queue<Point> q = new ArrayDeque<>();
		q.offer(new Point(x, y));

		visited[y][x] = true;
		int cnt = 1;
		
		while (!q.isEmpty()) {
			Point cur = q.poll();
			
			for (int i = 0; i < 4; i++) {
				int ny = cur.y + dy[i];
				int nx = cur.x + dx[i];
				if (ny < 0 || ny > 4 || nx < 0 || nx > 4 || visited[ny][nx] || site[y][x] != site[ny][nx]) continue;
				visited[ny][nx] = true;
				q.offer(new Point(nx, ny));
				cnt++;
			}
		}
		return cnt < 3 ? 0 : cnt;
	}
	
	static int deleteRelic(int y, int x) {				// 유적 없애기
		Queue<Point> q = new ArrayDeque<>();
		q.offer(new Point(x, y));

		visited[y][x] = true;
		List<Point> delete = new ArrayList<>();
		delete.add(new Point(x, y));
		
		while (!q.isEmpty()) {
			Point cur = q.poll();
			
			for (int i = 0; i < 4; i++) {
				int ny = cur.y + dy[i];
				int nx = cur.x + dx[i];
				if (ny < 0 || ny > 4 || nx < 0 || nx > 4 || visited[ny][nx] || site[y][x] != site[ny][nx]) continue;
				visited[ny][nx] = true;
				q.offer(new Point(nx, ny));
				delete.add(new Point(nx, ny));
			}
		}
		if (delete.size() < 3) return 0;
		
		for (Point p : delete) {
			site[p.y][p.x] = 0;
		}
		
		return delete.size();
	}
	
	static void fillRelic() {						// 유적 채우기
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (site[4-j][i] == 0) {
					site[4-j][i] = wall[pointer++];
				}
			}
		}
	}
}