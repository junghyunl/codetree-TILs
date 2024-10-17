import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

	static int N, M, P, C, D, rr, rc;
	static int[][] map;
	static Santa[] santas;
	static int[] dy = {-1,-1,0,-1,1,1,0,1};
	static int[] dx = {0,1,1,-1,0,-1,-1,1};
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		P = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		D = Integer.parseInt(st.nextToken());
		santas = new Santa[P];
		map = new int[N][N];
		
		st = new StringTokenizer(br.readLine());
		rr = Integer.parseInt(st.nextToken())-1;
		rc = Integer.parseInt(st.nextToken())-1;
		
		for (int i = 0; i < P; i++) {
			st = new StringTokenizer(br.readLine());
			int pn = Integer.parseInt(st.nextToken())-1;
			int sr = Integer.parseInt(st.nextToken())-1;
			int sc = Integer.parseInt(st.nextToken())-1;
			santas[pn] = new Santa(sr, sc);
			map[sr][sc] = pn+1;
		}
		
		for (int i = 0; i < M; i++) {
			
			int santaIdx = findSanta();
			if (santaIdx == -1) break;								//산타가 모두 탈락하면 종료
			
			rudolphMove(santas[santaIdx].y, santas[santaIdx].x);	//루돌프 이동
			
			for (int j = 0; j < P; j++) {
				if (santas[j].status == -1) continue;
				else if (santas[j].status > 0) {
					santas[j].status--;
					continue;
				}else {
					santaMove(j);
				}
			}
			for (int j = 0; j < P; j++) {
				if (santas[j].status == -1) continue;
				santas[j].score++;
			}
		}
		
		StringBuilder ans = new StringBuilder();
		for (int i = 0; i < P; i++) {
			ans.append(santas[i].score).append(" ");
		}
		System.out.println(ans);
	}
	static int findSanta() {
		int santaIdx = -1;
		int distance = Integer.MAX_VALUE;
		for (int i = 0; i < P; i++) {
			if (santas[i].status == -1) continue;
			int curDist = getDistance(rr, rc, santas[i].y, santas[i].x);
			if (curDist < distance) {
				santaIdx = i;
				distance = curDist;
			}else if (curDist == distance) {
				if (santas[i].y > santas[santaIdx].y || (santas[i].y == santas[santaIdx].y && santas[i].x > santas[santaIdx].x)) {
					santaIdx = i;
				}
			}
		}
		return santaIdx;
	}
	static void rudolphMove(int sr, int sc) {
		int moveIdx = -1;
		int distance = Integer.MAX_VALUE;
		for (int i = 0; i < 8; i++) {
			int ny = rr + dy[i];
			int nx = rc + dx[i];
			if (ny < 0 || ny >= N || nx < 0 || nx >= N) continue;
			
			int curDist = getDistance(sr, sc, ny, nx);
			if (curDist < distance) {
				moveIdx = i;
				distance = curDist;
			}
		}
		rr += dy[moveIdx];
		rc += dx[moveIdx];
		
		if (map[rr][rc] > 0) {
			int santaIdx = map[rr][rc]-1;
			map[rr][rc] = 0;
			santas[santaIdx].score += C;
			santas[santaIdx].status = 2;
			crash(santaIdx, moveIdx, C);
		}
	}
	static void santaMove(int index) {
		Santa s = santas[index];
		int moveIdx = -1;
		int distance = getDistance(rr, rc, s.y, s.x);
		for (int i = 0; i < 8; i+=2) {
			int ny = s.y + dy[i];
			int nx = s.x + dx[i];
			if (ny < 0 || ny >= N || nx < 0 || nx >= N || map[ny][nx] > 0) continue;
			int curDist = getDistance(rr, rc, ny, nx);

			if (curDist < distance) {
				moveIdx = i;
				distance = curDist;
			}
		}
		if (moveIdx == -1 || map[s.y+dy[moveIdx]][s.x+dx[moveIdx]] > 0) return;
		
		map[s.y][s.x] = 0;
		s.y += dy[moveIdx];
		s.x += dx[moveIdx];
		
		if (s.y == rr && s.x == rc) {
			santas[index].score += D;
			santas[index].status = 1;
			crash(index, (moveIdx+4)%8, D);
		}else {
			map[s.y][s.x] = index+1;
		}
	}
	static void crash(int index, int dir, int push) {
		Santa s = santas[index];
		
		s.y += push*dy[dir];
		s.x += push*dx[dir];
		
		if (s.y < 0 || s.y >= N || s.x < 0 || s.x >= N) {
			s.status = -1;
		}else {
			if (map[s.y][s.x] > 0) {
				crash(map[s.y][s.x]-1, dir, 1);
			}
			map[s.y][s.x] = index+1;
		}
	}
 	static int getDistance(int sr, int sc, int er, int ec) {
		return (int)(Math.pow(sr-er, 2) + Math.pow(sc-ec, 2));
	}
	static class Santa {
		int y, x, status, score;

		public Santa(int y, int x) {
			this.y = y;
			this.x = x;
			this.status = 0;
			this.score = 0;
		}
	}
}