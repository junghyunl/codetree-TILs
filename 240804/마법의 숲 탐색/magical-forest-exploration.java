import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
	
	private static int[][] forest;
	private static int r, c;
	private static final int[][] dx = {{1,2,1}, {-1,0,1,1,2}, {-1,0,1,1,2}};
	private static final int[][] dy = {{-1,0,1}, {-1,-2,-2,-1,-1}, {1,2,2,1,1}};
	private static final int[] ddx = {-1,0,1,0};
	private static final int[] ddy = {0,1,0,-1};
	
	public static boolean check(int a, int b, int op) {
		for (int i = 0; i < dx[op].length; i++) {
			int nx = a + dx[op][i];
			int ny = b + dy[op][i];
			
			if (nx < 0 && (ny >0 || ny < c)) continue;
			if (nx >= r || ny < 0 || ny >= c) return false;
			if (forest[nx][ny] > 0) return false;
		}
		return true;
	}
	
	public static int bfs(int a, int b) {
		Queue<int[]> queue = new LinkedList<>();
		boolean[][] visited = new boolean[r][c];
		int maxRow = a;
		
		queue.add(new int[] {a, b, forest[a][b]});
		visited[a][b] = true;
		
		while (!queue.isEmpty()) {
			int[] current = queue.poll();
			int x = current[0];
			int y = current[1];
			int cnt = current[2];
			
			for (int i = 0; i < 4; i++) {
				int nx = x + ddx[i];
				int ny = y + ddy[i];
				
				if (nx >= 0 && nx < r && ny >= 0 && ny < c && !visited[nx][ny] && forest[nx][ny] != 0) {
				    if (forest[nx][ny] == cnt || cnt == -1 || forest[nx][ny] == -1) {
				        queue.add(new int[] {nx, ny, forest[nx][ny]});
				        visited[nx][ny] = true;
				        maxRow = Math.max(maxRow, nx);
				    }
				}
			}
		}
		return maxRow+1;
	}
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		r = scan.nextInt();
		c = scan.nextInt();
		forest = new int[r][c];
		int k = scan.nextInt();
		int ans = 0, golemCnt = 0;
		
		for (int i = 0; i < k; i++) {
			int x = -2;
			int y = scan.nextInt()-1;
			int exit = scan.nextInt();
			
			while (true) {
				if (check(x,y,0)) {
					x++;
				}else if (check(x,y,1)) {
					x++;
					y--;
					exit = (exit-1)%4;
				}else if (check(x,y,2)) {
					x++;
					y++;
					exit = (exit+1)%4;
				}else {
					break;
				}
			}
			
			if (x <= 0) {
				golemCnt = 0;
				forest = new int[r][c];
			} else {
				golemCnt++;
				forest[x][y] = golemCnt;
				for (int j = 0; j < 4; j++) {
					forest[x + ddx[j]][y + ddy[j]] = golemCnt;
				}
				forest[x + ddx[exit]][y + ddy[exit]] = -1;
				ans += bfs(x,y);
			}
 		}
		
		System.out.println(ans-1);
	}
}