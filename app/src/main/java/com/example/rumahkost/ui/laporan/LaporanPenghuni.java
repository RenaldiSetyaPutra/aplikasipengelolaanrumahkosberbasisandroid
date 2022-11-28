package com.example.rumahkost.ui.laporan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumahkost.R;
import com.example.rumahkost.ui.penghuni.Model_Penghuni;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class LaporanPenghuni extends AppCompatActivity {
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");
    RecyclerView rv;
    ArrayList<Model_Penghuni> listPenghuni;
    TabelAdapterPenghuni adapter;
    Button btnpdf;
    int pageWidth = 2480;
    int pageHeight = 3580;
    int no = 1;
    String id,nama,jk,asal,nohp,tglmsk,unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_penghuni);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        rv = findViewById(R.id.rvtabelpenghuni);
        rv.setLayoutManager(new LinearLayoutManager(LaporanPenghuni.this));
        RefreshList();

        btnpdf = findViewById(R.id.btnpdf);
        btnpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void RefreshList() {
        db.child("Penghuni").orderByChild("nama").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPenghuni = new ArrayList<>();
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    Model_Penghuni peng = ds.getValue(Model_Penghuni.class);
                    listPenghuni.add(peng);
                    no = no++;
                    id = peng.getId();
                    nama = peng.getNama();
                    jk = peng.getJk();
                    asal = peng.getAsal();
                    nohp = peng.getNo_hp();
                    tglmsk = peng.getTgl_msk();
                    unit = peng.getUnit();
                }
                adapter = new TabelAdapterPenghuni(listPenghuni, LaporanPenghuni.this);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void buatPDF() {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo
                = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Paint paint = new Paint();
        Paint title = new Paint();
        Canvas canvas = page.getCanvas();

        title.setTextAlign(Paint.Align.CENTER);
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setColor(Color.BLACK);
        title.setTextSize(70);
        canvas.drawText("Laporan Penghuni Rumah Kos H.Rojak", pageWidth / 2, 130, title);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRect(40, 180, pageWidth - 40, 280, paint);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(35f);
        canvas.drawText("No", 100, 300, paint);
        canvas.drawText("ID", 200, 300, paint);
        canvas.drawText("Nama Penghuni", 430, 300, paint);
        canvas.drawText("Jenis Kelamin", 780, 300, paint);
        canvas.drawText("Asal", 1050, 300, paint);
        canvas.drawText("No Hp", 1250, 300, paint);
        canvas.drawText("Tanggal Masuk", 1550, 300, paint);
        canvas.drawText("Unit", 1830, 300, paint);


        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(35f);
        canvas.drawText(String.valueOf(no), 100, 400, paint);
        canvas.drawText(id, 200, 400, paint);
        canvas.drawText(nama, 430, 400, paint);
        canvas.drawText(jk, 780, 400, paint);
        canvas.drawText(asal, 1050, 400, paint);
        canvas.drawText(nohp, 1250, 400, paint);
        canvas.drawText(tglmsk, 1550, 400, paint);
        canvas.drawText(unit, 1830, 400, paint);

        pdfDocument.finishPage(page);
        String myFilePath = Environment.getExternalStorageDirectory().getPath() + "/Laporan Penghuni.pdf";
        File myFile = new File(myFilePath);
        try {
            pdfDocument.writeTo(new FileOutputStream(myFile));
            Toast.makeText(LaporanPenghuni.this, "PDF Berhasil Dibuat", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(LaporanPenghuni.this, "Error", Toast.LENGTH_SHORT).show();
        }
        pdfDocument.close();
    }
}
