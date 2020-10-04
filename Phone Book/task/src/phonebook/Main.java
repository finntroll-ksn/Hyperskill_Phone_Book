package phonebook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    private static final DateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("mm 'min.' ss 'sec.' SSS 'ms.'");

    static private List<String> names;
    static private Map<String, Long> phoneBook = new HashMap<>();
    static private Map<String, Long> treePhoneBook = new TreeMap<>();

    public static void main(String[] args) throws IOException {
        readDataFromFiles();
        linearSearch();
        jumpSearch();
        binarySearch();
        search();
    }

    private static void readDataFromFiles() throws IOException {
        try {
            names = Files.readAllLines(Path.of("find.txt"));
        } catch (Exception ex) {
            System.out.println("'There is no such file exception'\nBye!");
            System.exit(0);
        }

        try (
                FileInputStream fis = new FileInputStream(new File("directory.txt"));
                Scanner scanner = new Scanner(fis)
        ) {
            while (scanner.hasNext()) {
                String[] words = scanner.nextLine().split("\\s+", 2);
                phoneBook.put(words[1], Long.parseLong(words[0]));
                treePhoneBook.put(words[1], Long.parseLong(words[0]));
            }
        }
    }

    private static void search() {
        System.out.println("Start searching (hash table)...");
        int found = 0;

        long startTime = System.currentTimeMillis();
        long sortTime = System.currentTimeMillis();

        for (String name : names) {
            found += phoneBook.containsKey(name) ? 1 : 0;
        }

        long endTime = System.currentTimeMillis();

        Date sortDuration = new Date(endTime - sortTime);
        Date searchDuration = new Date(sortTime - startTime);
        Date duration = new Date(endTime - startTime);
        System.out.printf(
                "Found %d / %d entries. Time taken: %s\nCreating time: %s\nSearching time: %s\n\n",
                found,
                names.size(),
                SIMPLE_DATE_FORMAT.format(duration),
                SIMPLE_DATE_FORMAT.format(sortDuration),
                SIMPLE_DATE_FORMAT.format(searchDuration)
        );
    }

    private static void linearSearch() {
        System.out.println("Start searching (linear search)...");
        int found = 0;

        long startTime = System.currentTimeMillis();

        for (String name : names) {
            for (String person : phoneBook.keySet()) {
                if (person.equals(name)) {
                    found++;
                    break;
                }
            }
        }

        long endTime = System.currentTimeMillis();

        Date duration = new Date(endTime - startTime);
        System.out.printf("Found %d / %d entries. Time taken: %s\n\n", found, names.size(), SIMPLE_DATE_FORMAT.format(duration));
    }

    private static void jumpSearch() {
        System.out.println("Start searching (bubble sort + jump search)...");
        int found = 0;
        int arraySize = treePhoneBook.keySet().size();
        int step = (int) Math.sqrt(arraySize);

        long startTime = System.currentTimeMillis();

        String[] persons = treePhoneBook.keySet().toArray(new String[arraySize]);

        long sortTime = System.currentTimeMillis();

        for (String name : names) {
            for (int i = 1; i < arraySize / step; i++) {
                if (persons[i * step].equals(name)) {
                    found++;
                    break;
                } else {
                    if (persons[i * step].compareTo(name) > 0) {
                        for (int j = 0; j < step; j++) {
                            if (persons[i * step - j].equals(name)) {
                                found++;

                                break;
                            }
                        }
                    }
                }
            }

            if (persons[arraySize - 1].compareTo(name) > 0) {
                for (int j = 0; j < step; j++) {
                    if (persons[arraySize - 1 - j].equals(name)) {
                        found++;

                        break;
                    }
                }
            }
        }

        long endTime = System.currentTimeMillis();

        Date sortDuration = new Date(endTime - sortTime);
        Date searchDuration = new Date(sortTime - startTime);
        Date duration = new Date(endTime - startTime);
        System.out.printf(
                "Found %d / %d entries. Time taken: %s\nSorting time: %s\nSearching time: %s\n\n",
                found,
                names.size(),
                SIMPLE_DATE_FORMAT.format(duration),
                SIMPLE_DATE_FORMAT.format(sortDuration),
                SIMPLE_DATE_FORMAT.format(searchDuration)
        );
    }

    private static void binarySearch() {
        System.out.println("Start searching (quick sort + binary search)...");
        int found = 0;
        int arraySize = treePhoneBook.keySet().size();

        long startTime = System.currentTimeMillis();

        String[] persons = treePhoneBook.keySet().toArray(new String[arraySize]);

        long sortTime = System.currentTimeMillis();

        for (String name : names) {
            int left = 0;
            int right = persons.length;

            while (left <= right) {
                int mid = left + (right - left) / 2;

                if (name.equals(persons[mid])) {
                    found++;

                    break;
                } else if (name.compareTo(persons[mid]) < 0) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }

        long endTime = System.currentTimeMillis();

        Date sortDuration = new Date(endTime - sortTime);
        Date searchDuration = new Date(sortTime - startTime);
        Date duration = new Date(endTime - startTime);
        System.out.printf(
                "Found %d / %d entries. Time taken: %s\nSorting time: %s\nSearching time: %s\n\n",
                found,
                names.size(),
                SIMPLE_DATE_FORMAT.format(duration),
                SIMPLE_DATE_FORMAT.format(sortDuration),
                SIMPLE_DATE_FORMAT.format(searchDuration)
        );
    }
}
