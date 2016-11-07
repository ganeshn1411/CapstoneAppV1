package jialiw.cmu.edu.capstoneappv1;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class ExcelFileImportTest extends AppCompatActivity  {
    final static LinkedList<String> readResult = new LinkedList<>();
    Button readExcelButton;
    static String TAG = "ExcelLog";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel_file_import_test);
        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        Log.e("ExcelFileImportTest", "in line 37: " + username);

        readExcelButton = (Button) findViewById(R.id.read_excel_button);
        readExcelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                readExcelFile(ExcelFileImportTest.this,"myexcel.xls");
                Log.e("in ExcelFile", "exit readExcelFile method");

                Intent excelIntent = new Intent(ExcelFileImportTest.this, ActivityDetailsTest.class);
                excelIntent.putExtra("username",username);
                excelIntent.putExtra("info_list",readResult);
                Log.e("go through intent", "hahaha");
            }

        });
    }

    private static void readExcelFile (Context context, String fileName) {
        if (!isExternalStorageAvailable() || isExternalStoageReadOnly()) {
            Log.e(TAG, "Storage not available or read only");
            return;
        }

        try {
            //Create Input Stream
            File file = new File(context.getExternalFilesDir(null), fileName);
            FileInputStream myInput = new FileInputStream(file);

            //Create a POIFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            //Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            //Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            /** We now need something to iterate through the cells. **/
            Iterator rowIter = mySheet.rowIterator();
            while (rowIter.hasNext()){
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator cellIter = myRow.cellIterator();
                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    Log.d(TAG, "Cell Value: " + myCell.toString());
                    Toast.makeText(context, "Cell Value: " + myCell.toString(), Toast.LENGTH_SHORT).show();
                }
                readResult.offer(rowIter.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)){
            return true;
        }
        return false;
    }

    public static boolean isExternalStoageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)){
            return true;
        }
        return false;
    }
}
