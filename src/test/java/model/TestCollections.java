package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TestCollections {

    // 1 --------------------------------
    @Test
    void testPrintList() {
        list.forEach(System.out::println);
    }

    // 2 --------------------------------
    @Test
    void testChangeWeightOfFirstByOne() {
        //todo Изменить вес первой коробки на 1.
        list.get(0).setWeight(5);
        assertEquals(new HeavyBox(1,2,3,5), list.get(0));
    }

    // 3 --------------------------------
    @Test
    void testDeleteLast() {
        //todo Удалить предпоследнюю коробку.
        list.remove(list.get(list.size()-1));
        assertEquals(6, list.size());
        assertEquals(new HeavyBox(1,2,3,4), list.get(0));
        assertEquals(new HeavyBox(1,3,3,4), list.get(list.size()-2));
    }

    // 4 --------------------------------
    @Test
    void testConvertToArray() {
        //todo Получить массив содержащий коробки из коллекции тремя способами и вывести на консоль.
        HeavyBox[] arr = new HeavyBox[list.size()];
//      1
        arr = list.toArray(arr);
//      2
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }

//      3
        arr = list.stream().toArray(HeavyBox[]::new);

        assertArrayEquals(new HeavyBox[]{
                new HeavyBox(1,2,3,4),
                new HeavyBox(3,3,3,4),
                new HeavyBox(2,6,5,3),
                new HeavyBox(2,3,4,7),
                new HeavyBox(1,3,3,4),
                new HeavyBox(1,2,3,4),
                new HeavyBox(1,1,1,1)
        }, arr);
    }

    // 5 --------------------------------
    @Test
    void testDeleteBoxesByWeight() {
        // todo удалить все коробки, которые весят 4
        Iterator<HeavyBox> iter = list.iterator();
        while (iter.hasNext()) {
            HeavyBox box = iter.next();
            if (box.getWeight() == 4) {
                iter.remove();
            }
        }

        assertEquals(3, list.size());
    }

    // 6 --------------------------------
    @Test
    void testSortBoxesByWeight() {
        // отсортировать коробки по возрастанию веса. При одинаковом весе - по возрастанию объема

        Collections.sort(list, new Comparator<HeavyBox>() {
            @Override
            public int compare(HeavyBox o1, HeavyBox o2) {
                if (o1.getWeight() > o2.getWeight())
                    return 1;
                else if (o1.getWeight() < o2.getWeight())
                    return -1;
                else {
                    if (o1.getVolume() > o2.getVolume())
                        return 1;
                    else if (o1.getVolume() < o2.getVolume())
                        return -1;
                    else
                        return 0;
                }
            }
        });
        assertEquals(new HeavyBox(1,1,1,1), list.get(0));
        assertEquals(new HeavyBox(2,3,4,7), list.get(6));
        assertEquals(new HeavyBox(1,2,3,4), list.get(3));
        assertEquals(new HeavyBox(1,3,3,4), list.get(4));
    }

    // 7 --------------------------------
    @Test
    void testClearList() {
        //todo Удалить все коробки.
        list.clear();
        assertTrue(list.isEmpty());
    }

    // 8 --------------------------------
    @Test
    void testReadAllLinesFromFileToList() {
        // todo Прочитать все строки в коллекцию
        List<String> lines = new ArrayList<>();

        try {
            String line = reader.readLine();

            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(19, lines.size());
        assertEquals("", lines.get(8));
    }

    // 9 --------------------------------
    @Test
    void testReadAllWordsFromFileToList() throws IOException {
        // todo прочитать все строки, разбить на слова и записать в коллекцию
        List<String> words = readAllWordsFromFileToList();
        assertEquals(257, words.size());
    }

    List<String> readAllWordsFromFileToList() throws IOException {
        List<String> result = new ArrayList<>();

        String line = reader.readLine();
        while (line != null) {
            String[] words = line.split(REGEXP);
            result.addAll(Arrays.asList(words));
            result = result.stream().filter(word -> !word.isEmpty()).collect(Collectors.toList());
            line = reader.readLine();
        }

        return result;
    }

    // 10 -------------------------------
    @Test
    void testFindLongestWord() throws IOException {
        // todo Найти самое длинное слово
        assertEquals("conversations", findLongestWord());
    }

    private String findLongestWord() throws IOException {
        List<String> words= readAllWordsFromFileToList();
//      1
        String longest = words.get(0);
        for (String word : words) {
            if (word.length() > longest.length())
                longest = word;
        }
//        return longest;

//      2
        return words.stream().max(Comparator.comparingInt(String::length)).get();
    }

    // 11 -------------------------------
    @Test
    void testAllWordsByAlphabetWithoutRepeat() throws IOException {
        // todo Получить список всех слов по алфавиту без повторов
        List<String> result = null;

        List<String> words = readAllWordsFromFileToList();
        Set<String> set = new TreeSet<>();

        for (String word : words) {
            set.add(word.toLowerCase());
        }

        result = set.stream().toList();

        assertEquals("alice", result.get(5));
        assertEquals("all", result.get(6));
        assertEquals("without", result.get(134));
        assertEquals(138, result.size());
    }

    // 12 -------------------------------
    @Test
    void testFindMostFrequentWord() throws IOException {
        // todo Найти самое часто вcтречающееся слово
        assertEquals("the", mostFrequentWord());
    }

    // 13 -------------------------------
    @Test
    void testFindWordsByLengthInAlphabetOrder() throws IOException {
        // todo получить список слов, длиной не более 5 символов, переведенных в нижний регистр, в порядке алфавита, без повторов
        List<String> words = readAllWordsFromFileToList();

        Set<String> set = new TreeSet<>();

        for (String word : words) {
            if (word.length() <= 5) {
                set.add(word.toLowerCase());
            }
        }

        words = set.stream().toList();

        assertEquals(94, words.size());
        assertEquals("a", words.get(0));
        assertEquals("alice", words.get(2));
        assertEquals("would", words.get(words.size() - 1));
    }

    private String mostFrequentWord() throws IOException {
        List<String> words = readAllWordsFromFileToList();
        Map<String, Long> frequency = words.stream()
                .collect(Collectors.groupingBy(String::toLowerCase, Collectors.counting()));


        Optional<Map.Entry<String, Long>> maxEntry = frequency.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        return maxEntry.get().getKey();
    }

    List<HeavyBox> list;

    @BeforeEach
    void setUp() {
        list = new ArrayList<>(List.of(
                new HeavyBox(1,2,3,4),
                new HeavyBox(3,3,3,4),
                new HeavyBox(2,6,5,3),
                new HeavyBox(2,3,4,7),
                new HeavyBox(1,3,3,4),
                new HeavyBox(1,2,3,4),
                new HeavyBox(1,1,1,1)
        ));
    }

    static final String REGEXP = "\\W+"; // for splitting into words

    private BufferedReader reader;

    @BeforeEach
    public void setUpBufferedReader() throws IOException {
        reader = Files.newBufferedReader(
                Paths.get("Text.txt"), StandardCharsets.UTF_8);
    }

    @AfterEach
    public void closeBufferedReader() throws IOException {
        reader.close();
    }
}
