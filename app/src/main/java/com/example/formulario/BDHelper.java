package com.example.formulario;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
/** * Created by RAUL hacemos que herede de SQliteOpenHelper */
public class BDHelper extends SQLiteOpenHelper{
    /*Exportamos todos los metodos de la clase que heredo*/
    public BDHelper(@Nullable Context context, @Nullable String name,
                    @Nullable SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    /*Oncreate metodo para crear la base de datos al lanzar la actividad */
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*db es un objeto de SQLiteDatabase lo resto es sentencias de sql*/
        db.execSQL("create table registro(codigo integer primary key unique," +
                "nombre text not null," +
                "apellido text not null," +
                "nota2 integer not null," +
                "nota3 integer not null," +
                "promedio integer  not null)");
    }

    /*si la tabla existe primero lo borramos*/
    /*luego creamos nuevamente la base de datos*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists registro");
        db.execSQL("create table registro(codigo integer primary key unique," +
                "nombre text not null," +
                "apellido text  not null," +
                "nota2 integer  not null," +
                "nota3 integer  not null," +
                "promedio integer  not null)");
    }

    //metodo listar registro de la db para extraer y mostrar en el listview
    public ArrayList<String> getAllRegistros()
    {
        /*creamos un arreglo de  string */
        ArrayList<String> array_list = new ArrayList<String>();
        /*creamos un objeto de la base de datos y lo inicializamos
         con el metodo que abre la base de datos para lectura */
        SQLiteDatabase db = this.getReadableDatabase();
        /*creamos un objeto de cursor y lo pasamos la query del select*/
        Cursor res =  db.rawQuery( "select * from registro", null );
        /*movemos el cursor a la primera fila*/
        res.moveToFirst();
        /*hacemos un bucle while para la busqueda de datos */
        while(res.isAfterLast() == false){
            /*miestras encuentre datos que capture id es el 0 curso es 1 */
            array_list.add("Id:" + res.getString(0) +" | "+"Nombre "+""+
                    res.getString(1) + " \n "+ " Apellido " + res.getString(2) + "\n" +
                    "Promedio"+ res.getString(5));
            /*movemos el cursor a la siguiente linea*/
            res.moveToNext();}
        /*retornamos el arreglo de string*/
        return array_list;
    }
}
