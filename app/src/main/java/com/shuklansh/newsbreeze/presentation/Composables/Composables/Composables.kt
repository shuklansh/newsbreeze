import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.shuklansh.newsbreeze.R
import com.shuklansh.newsbreeze.presentation.NewsViewModel
import com.shuklansh.newsbreeze.ui.theme.*
import kotlinx.coroutines.flow.collect

@Composable
fun bookmarkIcon(bookmark: Boolean) {
    Box(
        modifier = Modifier
            .width(52.dp)
            .height(52.dp)
            .clip(
                RoundedCornerShape(8.dp)
            )
            .background(
                if (bookmark) {
                    myGreen
                } else {
                    myGray
                }
            )
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            var bookmarked = bookmark
            IconButton(onClick = {
                bookmarked = !bookmarked
            }) {
                if (bookmarked) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        tint = myAppBg,
                        painter = painterResource(id = R.drawable.bookmark_fill),
                        contentDescription = "Save"
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        tint = myAppBg,
                        painter = painterResource(id = R.drawable.bookmark),
                        contentDescription = "unsaved"
                    )
                }
            }
        }
    }
}

@Composable
fun topAppBar(nav: NavController) {
    var clers = FontFamily(
        Font(R.font.clers, FontWeight.Bold)
    )
    Row(Modifier.fillMaxWidth()) {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.16f),
            elevation = 0.dp,
            title = {
                Text(
                    text = "NewsBreeze",
                    fontFamily = clers,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp
                )
            },
            actions = {

                Box(
                    modifier = Modifier.padding(horizontal = 12.dp)
                        .width(52.dp)
                        .height(52.dp)
                        .clip(
                            RoundedCornerShape(8.dp)
                        )
                        .background(myGreen)
                ) {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        IconButton(onClick = { nav.navigate(R.id.action_dashboardScreen_to_savedScreen) }) {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                tint = myAppBg,
                                painter = painterResource(id = R.drawable.bookmark),
                                contentDescription = "unsaved"
                            )
                        }
                    }
                }

            }
        )
    }
}


@Composable
fun searchBar(queryEntered: String, vm : NewsViewModel) {
    var query = queryEntered
    var akatab = FontFamily(
        Font(R.font.akatabregular)
    )
    TextField(value = query,
        onValueChange = {
            query = it
            vm.updateQuery(query)
        },
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.14f)
            .padding(4.dp)
            .clip(RoundedCornerShape(50f))
            .background(
                Color.Red
            ),
        maxLines = 1,
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = myLightGray,
            textColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            disabledLabelColor = Color.Transparent,
            cursorColor = Color.Black,
            disabledPlaceholderColor = Color.Transparent,
            focusedLabelColor = Color.Transparent,
            unfocusedLabelColor = Color.Transparent,
        ),
        keyboardActions = KeyboardActions(
            onDone = {vm.getNews(query)},
            onGo = {vm.getNews(query)},
            onSend = {vm.getNews(query)},
            onSearch = {vm.getNews(query)},
        ),
        placeholder = {
            Text(
                text = "Search",
                color = myGray,
                fontFamily = akatab,
                fontSize = 20.sp
            )
        },
        leadingIcon = {
            IconButton(onClick = {
                vm.getNews(query)
            }) {
                Icon(
                    tint = myGray,
                    imageVector = Icons.Default.Search,
                    contentDescription = ""
                )
            }
        },
        trailingIcon = {
            IconButton(onClick = { vm.getNews(query) }) {
                Icon(
                    tint = myGray,
                    imageVector = Icons.Default.Tune,
                    contentDescription = ""
                )
            }
        }
    )
}


@Composable
fun myDivider() {
    Divider(
        color = myCol,
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .fillMaxWidth()  //fill the max height
            .height(8.dp)
            .clip(RoundedCornerShape(50f))
    )
}

