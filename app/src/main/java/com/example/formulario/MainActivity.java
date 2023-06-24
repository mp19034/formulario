package com.example.formulario;


import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //varoables locales de los controles o clases
    private EditText et1,et2,et3,et4,et5,et6;
    private Cursor fila;
    ListView listaView;
    /*creamos las variables de la base de datos*/
    BDHelper admin;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //instanciamos las variables con los id de la vista
        et1= (EditText) findViewById(R.id.edtcodigo);
        et2= (EditText) findViewById(R.id.nombre);
        et3= (EditText) findViewById(R.id.apellido);
        et4= (EditText) findViewById(R.id.edtnota2);
        et5= (EditText) findViewById(R.id.edtnota3);
        et6= (EditText) findViewById(R.id.edtpromedio);
        listaView = (ListView) findViewById(R.id.lvtablanotas);
        //para que el campo promedio no pueda ser editado
        et6.setEnabled(false);
        //llamamos al metodo getAllRegistros que lista
        admin = new BDHelper(this, "instituto", null, 1);
        //obtenemos todos lo datos registrados de la base de datos.
        ArrayList array_list = admin.getAllRegistros();
        /*creamos un arreglo de string e instanciamos la forma de lista*/
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, array_list);
        /*a listview los pasamos el arreglo de string instanciado*/
        listaView.setAdapter(arrayAdapter);
    }

    //metodo guardar los datos
    public void Alta(View view) {
        /*instanciamos la variables de dbhelper y
        le pasamos el contexto , nombre de base de datos*/
        admin = new BDHelper(this, "instituto",
                null, 1);
        /*abrimos la base de datos pora escritura*/
        db = admin.getWritableDatabase();
        /*capturamos los datos de los edittext*/
        String codigo = et1.getText().toString();
        String nombre = et2.getText().toString();
        String apellido = et3.getText().toString();
        String nota2 = et4.getText().toString();
        String nota3 = et5.getText().toString();
        //calculamos el promedio
        /*creamosd una variable inicilizado con cero*/
        int promedioope=0;
        /*capturamos los valores y lo sumamos*/
        //promedioope=Integer.parseInt(et3.getText().toString());
        promedioope=promedioope+Integer.parseInt(et4.getText().toString());
        promedioope=promedioope+Integer.parseInt(et5.getText().toString());
        /*dividimpos la cantoidad de notas con la suma*/
        promedioope=promedioope/2;
        //convertir a string
        String promedio=String.valueOf(promedioope);
        //insertamos en la db los datos capturados
        ContentValues registro = new ContentValues();
        registro.put("codigo", codigo);
        registro.put("nombre", nombre);
        registro.put("apellido", apellido);
        registro.put("nota2", nota2);
        registro.put("nota3", nota3);
        registro.put("promedio", promedio);
        /*realizamos el insert en el resgitro*/
        db.insert("registro", null, registro);
        /*cerramos la base de datos*/
        db.close();
        /*listamos los datos registrados*/
        ArrayList array_list = admin.getAllRegistros();
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(
                this,android.R.layout.simple_list_item_1, array_list);
        listaView.setAdapter(arrayAdapter);
        /*limpiamos las cajas de textos*/
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et5.setText("");
        et6.setText("");
        /*lanzamos una notificacion toast para saber que se cargaron los datos*/
        Toast.makeText(this, "se cargaron los datos ", Toast.LENGTH_SHORT).show();
    }

    //metodo consultar la base de datos
    public void consulta(View v) {
        /*instanciamos la variables de dbhelper y
        le pasamos el contexto , nombre de base de datos*/
        admin = new BDHelper(this, "instituto", null, 1);
        /*abrimos la base de datos pora escritura*/
        db = admin.getWritableDatabase();
        /*creamos una variables string y lo
        inicializamos con los datos ingresados en edittext*/
        String codigo = et1.getText().toString();
        /*iniciamos la query en el cursor y indicamos el codigo digitado*/
        fila = db.rawQuery("select nombre,apellido,nota2,nota3,promedio  from registro where codigo=" + codigo, null);
        /*hacemos un if si hay datos que lea los datos del primer codigo*/
        if (fila.moveToFirst()) {
            et2.setText(fila.getString(0));
            et3.setText(fila.getString(1));
            et4.setText(fila.getString(2));
            et5.setText(fila.getString(3));
            et6.setText(fila.getString(4));
        }
        else
            /*si no existe entonces lanzara un toast*/
            Toast.makeText(this, "no existe un registro con dicho codigo",
                    Toast.LENGTH_SHORT).show();
        /*cerramos la base de datos*/
        db.close();
    }
    //metodo eliminar
    public void baja(View v) {
        /*instanciamos la variables de dbhelper y
        le pasamos el contexto , nombre de base de datos*/
        admin = new BDHelper(this, "instituto", null, 1);
        /*abrimos la base de datos pora escritura*/
        db = admin.getWritableDatabase();
        /*creamos una variables string y lo
        inicializamos con los datos ingresados en edittext*/
        String codigo = et1.getText().toString();
        /*variable int pasamos el metodo delete de la tabla registro el codigo que edittext*/
        int cant = db.delete("registro", "codigo=" + codigo, null);
        /*cerramos la DB*/
        db.close();
        /*listamos los datos de la base de datos*/
        ArrayList array_list = admin.getAllRegistros();
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(
                this,android.R.layout.simple_list_item_1, array_list);
        /*pasamos los datos a la listview para lo muestre*/
        listaView.setAdapter(arrayAdapter);
        /*limpiamos las cajas de texto*/
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et5.setText("");
        et6.setText("");
        /*si cante es igual que unos entonces*/
        if (cant == 1)
            /*lanzamos un toast que notifique se elimino*/
            Toast.makeText(this, "se borró el registro con dicho documento",
                    Toast.LENGTH_SHORT).show();
        else
            /*de lo contrario lanzara que esos datos no existen*/
            Toast.makeText(this, "no existe un registro con dicho documento",
                    Toast.LENGTH_SHORT).show();
    }
    //metodo modificar
    public void modificacion(View v) {
        /*instanciamos la variables de dbhelper y
        le pasamos el contexto , nombre de base de datos*/
        admin = new BDHelper(this, "instituto", null, 1);
        /*abrimos la base de datos pora escritura*/
        db = admin.getWritableDatabase();
        /*capturamos los datos de los edittext*/
        String codigo = et1.getText().toString();
        String nombre = et2.getText().toString();
        String apellido = et3.getText().toString();
        String nota2 = et4.getText().toString();
        String nota3 = et5.getText().toString();
        //calculamos el promedio
        int promedioope=0;
       // promedioope=Integer.parseInt(et3.getText().toString());
        promedioope=promedioope+Integer.parseInt(et4.getText().toString());
        promedioope=promedioope+Integer.parseInt(et5.getText().toString());
        promedioope=promedioope/3;
        //covertir a string
        String promedio=String.valueOf(promedioope);
        ContentValues registro = new ContentValues();
        registro.put("codigo", codigo);
        registro.put("curso", nombre);
        registro.put("apellido", apellido);
        registro.put("nota2", nota2);
        registro.put("nota3", nota3);
        registro.put("promedio", promedio);
        /*lanzamos el metodo update con el query para que actualice segun el id o codigo*/
        int cant = db.update("registro", registro, "codigo=" + codigo, null);
        /*cerramos la BD*/
        db.close();
        /*listamos los datos de la base de
        datos para mostrarlo en el listview*/
        ArrayList array_list = admin.getAllRegistros();
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(
                this,android.R.layout.simple_list_item_1, array_list);
        /*mostramos los datos de la db*/
        listaView.setAdapter(arrayAdapter);
        /*limpiamos la caja de texto*/
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et5.setText("");
        et6.setText("");
        /*si cante es igual que unos entonces*/
        if (cant == 1)
            /*si se cumple lka sentencia entonces muestra el toast*/
            Toast.makeText(this,"se modificaron los datos",Toast.LENGTH_SHORT)
                    .show();
        else
            /*de lo contrario muestra el siguiente toast*/
            Toast.makeText(this,"no existe un registro con dicho documento",
                    Toast.LENGTH_SHORT).show();
    }
}