package main;

public class Gambar {

    public String file_name;
    public int hl, hh, sl, sh, vl, vh,limit_area,component;
    String type,componentValue;

    public Gambar() {
        file_name = "";
        component = 0;
        hl = 0;
        hh = 0;
        sl = 0;
        sh = 0;
        vl = 0;
        vh = 0;
        limit_area=0;
        componentValue="";
        type = "";

    }

    public static void isikosong(Gambar gambar) {
        /*
        digunakan untuk mengosongkan elemen dengan mengisinya dengan karakter
        kosong dan 0, sehingga tidak terisi dengan "null" yang bisa menyebabkan 
        error saat pencarian nilai pada proses lain
         */
        gambar.file_name = "";
        gambar.component=0;
        gambar.hl = 0;
        gambar.hh = 0;
        gambar.sl = 0;
        gambar.sh = 0;
        gambar.vl = 0;
        gambar.vh = 0;
        gambar.limit_area=0;
        gambar.type = "";
        gambar.componentValue="";
    }

    public static void isikosong(Gambar gambar[]) {
        /*
        memiliki fungsi yang sama dengan isikosong sebelumnya, digunakan untuk
        mengosongkan Obyek yang berbentuk array
         */
        for (int i = 0; i < gambar.length; i++) {
            gambar[i] = new Gambar();
            //memanggil isikosong untuk tiap indeks
            isikosong(gambar[i]);
        }
    }
}
