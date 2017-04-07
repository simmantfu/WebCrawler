

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.processing.SupportedSourceVersion;

import java.util.Objects;
import java.util.Set;

// 1) Give the path.
// 2) Assgine each parent direactory to independent thread.
// 3) Traversall all the assgin direactors avaliable to independent thread.
// 4) Store each traves dir with number of dir/file avaliabe in it.
// 5) Sort the result in assending order.

public class SearchClass {

	static HashMap<Integer, String> hmap = new HashMap<>();
	static List<Thread> threadList = new ArrayList<>();
	static List<Thread> deadThreadList = new ArrayList<>();

	static class ParseDirs implements Runnable {

		String path = "";

		ParseDirs(String path) {
			this.path = path;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			searchPaths(path);
		}

	}



	public static void searchPaths(String path) {

		File file = new File(path);
		File[] fles = file.listFiles();
		if (Objects.nonNull(fles) && fles.length > 0) {
			hmap.put(fles.length, path);
			for (File f : fles) {

				if (f.isDirectory()) {
					System.out
							.println("Thread :- " + Thread.currentThread() + " Path : -" + f.getAbsolutePath());
					searchPaths(f.getAbsolutePath());
				}

			}

		}

	}

	public static void getPathRating() {

		Set<Integer> lit = hmap.keySet();
		ArrayList<Integer> arList = new ArrayList<>();
		arList.addAll(lit);
		Collections.sort(arList);
		System.out.println(hmap.get(arList.get(arList.size()-1)) + " number of files : " + (arList.get(arList.size()-1)));
		

	}

	public static void main(String... ar) {

		File f = new File("C://");
		File[] arFile = f.listFiles();

		if (Objects.nonNull(arFile) && arFile.length > 0) {
			for (File file : arFile) {
				Runnable run = new ParseDirs(file.getAbsolutePath());
				Thread thread = new Thread(run);
				threadList.add(thread);
				thread.start();
			}

			threadList.forEach(t -> {try {
				t.join();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}});
			
			getPathRating();
		}

	}

}
