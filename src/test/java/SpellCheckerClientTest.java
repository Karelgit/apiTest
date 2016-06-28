import com.yeezhao.guizhou.client.SpellCheckerClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by vectarrow on 2015-08-04.
 */
public class SpellCheckerClientTest {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("C:\\temp\\_input.txt");
        Scanner cin = new Scanner(new FileInputStream(input), "UTF-8");
        StringBuilder sb = new StringBuilder();

        while(cin.hasNextLine()) {
            sb.append(cin.nextLine()).append(System.lineSeparator());
        }
        System.out.println(new SpellCheckerClient().query(sb.toString()));
    }
}
