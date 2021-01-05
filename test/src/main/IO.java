package main;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Scanner;

public class IO {

    private final static Charset ENCODING = StandardCharsets.UTF_8;
    int i = 0;
    private Gambar x[];

    /**
     * Constructor.
     */
    public IO(int numberOfData) {
        x = new Gambar[numberOfData];
        for (int i = 0; i < x.length; i++) {
            x[i] = new Gambar();
        }
    }

    /**
     * Template method that calls {@link #processLine(String)}.
     *
     * @param nama_file
     * @throws java.io.IOException
     */
    public void processLineByLine(String nama_file) throws IOException {
        //Membaca 1 Garis penuh dengan menggunakan scanner
        //Paths.get: untuk membaca String nama_file untuk dijadikan lokasi file
        //ENCODING: digunakan sebagai sumber Character Set(ASCII, atau UTF8)
        try (Scanner scanner = new Scanner(Paths.get(nama_file), ENCODING.name())) {
            while (scanner.hasNextLine()) {
                processLine(scanner.nextLine());
            }
        }
    }

    protected void processLine(String aLine) {
        /*Scanner digunakan untuk membaca konten dalam 1 baris yang diberikan oleh
         method processLineByLine
         */
        Scanner scanner = new Scanner(aLine);
        //useDelimiter digunakan untuk menentukan jenis pemisah antara konten
        scanner.useDelimiter(",");
        if (scanner.hasNext()) {
            //membaca konten dan memasukkannnya ke dalam variabel yang sesuai
            x[i].file_name = scanner.next();
            x[i].hl = Integer.parseInt(scanner.next());
            x[i].hh = Integer.parseInt(scanner.next());
            x[i].sl = Integer.parseInt(scanner.next());
            x[i].sh = Integer.parseInt(scanner.next());
            x[i].vl = Integer.parseInt(scanner.next());
            x[i].vh = Integer.parseInt(scanner.next());
            x[i].type = scanner.next();
            x[i].limit_area = Integer.parseInt(scanner.next());
            x[i].component = Integer.parseInt(scanner.next());
            x[i].componentValue = scanner.next();
            /*setelah selesai membaca semua konten, iterasi nilai i untuk menguah 
             indeks array arr untuk pe,bacaan konten pada baris berikutnya*/
            i++;
        }
    }

    public static void tulis_database(Gambar[] x) throws FileNotFoundException,
            IOException {

        //path=lokasi dan nama file yang akan ditambah isinya
        String path = "database.txt";

        //membuat File dengan lokasi dan nama dari path
        File file = new File(path);

        /*
         menulis data pada file dengan BufferedWriter, sementara FileWriter
         digunakan untuk menentukan konfigurasi penulisan.
         argumen kedua digunkanan sebagai konfigurasi "append file".
         true, apabila ingin menulis tanpa menghapus isi file yang ada
         false, jika ingin menulis dari awal dengan menghapus semua isi lama
         */
        FileWriter fileWriter = new FileWriter(file, false);
        try (BufferedWriter bufferFileWriter = new BufferedWriter(fileWriter)) {
            for (int i = 0; i < x.length; i++) {
                if (x[i].file_name.equals("")) {
                    bufferFileWriter.append("");
                } else {
                    bufferFileWriter.append(x[i].file_name + "," + x[i].hl + ","
                            + x[i].hh + "," + x[i].sl + "," + x[i].sh + ","
                            + x[i].vl + "," + x[i].vh + "," + x[i].type +"," + x[i].limit_area+","+x[i].component+","+x[i].componentValue+ "\r\n");
                }
            }
            //setelah selesai proses, file ditutup
            bufferFileWriter.close();
        }
    }

    public static void tulis_baru(){

        //path=lokasi dan nama file yang akan ditambah isinya
        String path = "database.txt";

        //membuat File dengan lokasi dan nama dari path
        File file = new File(path);

        /*
         menulis data pada file dengan BufferedWriter, sementara FileWriter
         digunakan untuk menentukan konfigurasi penulisan.
         argumen kedua digunkanan sebagai konfigurasi "append file".
         true, apabila ingin menulis tanpa menghapus isi file yang ada
         false, jika ingin menulis dari awal dengan menghapus semua isi lama
         */
        
        try {
            FileWriter fileWriter = new FileWriter(file, false);
            BufferedWriter bufferFileWriter = new BufferedWriter(fileWriter);
            bufferFileWriter.append("");
            //setelah selesai proses, file ditutup
            bufferFileWriter.close();
        }catch (Exception ex){
            System.out.println("Warning, IO ERROR");
        }
    }

    public Gambar[] ret() {
        //mengembalikan nilai arr pada pemanggil
        return x;
    }
}
