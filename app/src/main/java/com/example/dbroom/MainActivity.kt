package com.example.dbroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.dbroom.roomDB.Pessoa
import com.example.dbroom.roomDB.PessoaDataBase
import com.example.dbroom.ui.theme.DBRoomTheme
import com.example.dbroom.viewModel.PessoaViewModel
import com.example.dbroom.viewModel.Repository

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            PessoaDataBase::class.java,
            "pessoa.db"
        ).build()
    }

    private val viewModel by viewModels<PessoaViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T: ViewModel> create(modelClass: Class<T>): T {
                    return PessoaViewModel(Repository(db)) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App(viewModel, this)
        }
    }
}

@Composable
fun App(viewModel: PessoaViewModel, mainActivity: MainActivity) {

    val corFundo = Color(0xFFbde0fe)

    var nome by remember{
        mutableStateOf("")
    }

    var telefone by remember{
        mutableStateOf("")
    }

    val pessoa = Pessoa(
        nome,
        telefone
    )

    var pessoaList by remember {
        mutableStateOf(listOf<Pessoa>())
    }

    viewModel.getPessoa().observe(mainActivity){
        pessoaList = it
    }

    Column(Modifier.background(corFundo))
    {
        Row(Modifier.padding(20.dp))
        {

        }

        Row(Modifier.fillMaxWidth(),
            Arrangement.Center
        ){
            Text(
                text = "App Database",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
        }

        Row (Modifier.padding(20.dp))
        {

        }

        Row (Modifier.fillMaxWidth(),
            Arrangement.Center
        ){
            TextField(
                value = nome,
                onValueChange = { nome = it},
                label = { Text("Nome:")}
            )
        }

        Row (Modifier.padding(20.dp))
        {

        }
        Row (Modifier.fillMaxWidth(),
            Arrangement.Center
        ){
            TextField(
                value = telefone,
                onValueChange = { telefone = it},
                label = { Text("Telefone:")}
            )
        }

        Row(Modifier.padding(20.dp))
        {

        }
        Row(Modifier.fillMaxWidth(),
            Arrangement.Center
        ){
            Button(onClick = {
                viewModel.upsertPessoa(pessoa)
                nome = ""
                telefone = ""
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0096c7),
                    contentColor = Color.White
                )) {
                Text("Cadastrar")
            }
        }

        Divider()
        LazyColumn {
            items(pessoaList){ pessoa ->
                Row (Modifier.fillMaxWidth(),
                    Arrangement.Center)
                {
                    Column(Modifier.fillMaxWidth(0.5f),
                        Arrangement.Center
                        ) {
                        Text(text = "${pessoa.nome}")
                    }
                    Column (Modifier.fillMaxWidth(0.5f),
                        Arrangement.Center)
                    {
                        Text(text = "${pessoa.telefone}")
                    }
                }
                Divider()
            }

        }

        Row(Modifier.padding(20.dp))
        {

        }
    }
}

@Preview
@Composable
fun AppPreview() {
    DBRoomTheme {

    }
}