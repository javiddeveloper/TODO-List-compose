package ir.javid.sattar.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.rememberNavBackStack
import dagger.hilt.android.AndroidEntryPoint
import ir.javid.sattar.todolist.common.navigation.Roots
import ir.javid.sattar.todolist.common.navigation.SetupNavGraph
import ir.javid.sattar.todolist.common.theme.TODOListTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TODOListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    val backStack = rememberNavBackStack(Roots.TodoList)
                    Box(modifier = Modifier.padding(it)){
                        SetupNavGraph(backStack = backStack)
                    }

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TODOListTheme {
        val backStack = rememberNavBackStack(Roots.TodoList)
        SetupNavGraph(backStack = backStack)
    }
}
