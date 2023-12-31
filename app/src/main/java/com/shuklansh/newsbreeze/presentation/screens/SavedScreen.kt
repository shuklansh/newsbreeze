
package com.shuklansh.newsbreeze.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import coil.compose.AsyncImage
import com.shuklansh.newsbreeze.R
import com.shuklansh.newsbreeze.presentation.NewsViewModel
import com.shuklansh.newsbreeze.presentation.user_events.UserEvent
import com.shuklansh.newsbreeze.ui.theme.NewsBreezeTheme
import com.shuklansh.newsbreeze.ui.theme.myAppBg
import com.shuklansh.newsbreeze.ui.theme.myGray
import com.shuklansh.newsbreeze.ui.theme.myGreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import topAppBar


@AndroidEntryPoint
class SavedScreen : Fragment() {

    var isarticleinDB = false

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val vm : NewsViewModel by viewModels()

        // Inflate the layout for this fragment
        return ComposeView(requireContext()).apply {
            setContent {
                var loaded by remember{ mutableStateOf(false) }
//                var sortval = vm.sort.collectAsState()
                var sortvalbydate by remember { mutableStateOf(false) }
                //var articlesList = remember{ mutableListOf(Article("","","","","","","",false)) }
                var savedArticles = vm.bmlist.collectAsState().value.articles
                LaunchedEffect(key1 = true ){
                    vm.onEvent(UserEvent.GetBookmarkArticlebyDate)
//                    vm.getAllArticlesFromDb()
                    loaded = true
                }

                var scope = rememberCoroutineScope()
                val onEventVM = vm::onEvent

                NewsBreezeTheme {
                    Scaffold(Modifier.fillMaxSize(),
                    topBar = {topAppBar(dash = false, detailed = false,saved = true,nav = findNavController() )}) {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .background(myAppBg)
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top) {
                            if(loaded){

                                    Column(
                                        Modifier
                                            .fillMaxSize()
                                            .padding(12.dp)) {
                                        if(!savedArticles!!.isEmpty()) {
                                        Row(Modifier.fillMaxWidth()){
                                            Text("Today", fontSize = 24.sp, color = Color.Black)
                                            Spacer(
                                                Modifier.height(
                                                    12.dp
                                                )
                                            )
                                            Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                                                IconButton(onClick = {
                                                    onEventVM(UserEvent.GetBookmarkArticlebyDate)
                                                    //scope.launch { vm.getAllArticlesFromDbbydate() }
                                                    sortvalbydate = true
                                                }) {
                                                    Icon(
                                                        tint = if(sortvalbydate) Color.Black else myGray,
                                                        imageVector = Icons.Default.CalendarMonth,
                                                        contentDescription = ""
                                                    )
                                                }
                                                Spacer(Modifier.height(12.dp))
                                                IconButton(onClick = {
                                                    onEventVM(UserEvent.GetBookmarkArticlebyOrderSave)
//                                                    scope.launch{ vm.getAllArticlesFromDb() }
                                                    sortvalbydate = false
                                                }) {
                                                    Icon(
                                                        tint = if(sortvalbydate) myGray else Color.Black,
                                                        imageVector = Icons.Default.Subtitles,
                                                        contentDescription = ""
                                                    )
                                                }
                                            }
                                        }
                                        Card(Modifier.fillMaxSize(), elevation = 8.dp,
                                        shape = RoundedCornerShape(12.dp), backgroundColor = myAppBg
                                        ) {

                                            LazyColumn {
                                                items(savedArticles!!) {
                                                    if (it != null) {
                                                        Column(
                                                            Modifier
                                                                .fillMaxWidth()
                                                                .fillMaxHeight()
                                                                .clip(
                                                                    RoundedCornerShape(16.dp)
                                                                )
                                                                .background(myAppBg)
                                                        ) {
                                                            Row(
                                                                Modifier
                                                                    .padding(12.dp)
                                                                    .clip(
                                                                        RoundedCornerShape(12.dp)
                                                                    )
                                                                    .clickable {
                                                                        val bundle = Bundle()
                                                                        bundle.putString(
                                                                            "image",
                                                                            it.urlToImage.toString()
                                                                        )
                                                                        bundle.putString(
                                                                            "title",
                                                                            it.title.toString()
                                                                        )
                                                                        bundle.putString(
                                                                            "content",
                                                                            it.content.toString()
                                                                        )
                                                                        bundle.putBoolean(
                                                                            "savedBool",
                                                                            true
                                                                        )
                                                                        bundle.putString(
                                                                            "publishedAt",
                                                                            it.publishedAt.toString()
                                                                        )
                                                                        bundle.putString(
                                                                            "author",
                                                                            it.author.toString()
                                                                        )
                                                                        bundle.putString(
                                                                            "url",
                                                                            it.url.toString()
                                                                        )
                                                                        bundle.putString(
                                                                            "description",
                                                                            it.description.toString()
                                                                        )
                                                                        findNavController().navigate(
                                                                            R.id.action_savedScreen_to_detailedScreen,
                                                                            bundle
                                                                        )
                                                                    }
                                                            ) {

                                                                AsyncImage(
                                                                    modifier = Modifier
                                                                        .width(132.dp)
                                                                        .height(132.dp)
                                                                        .clip(
                                                                            RoundedCornerShape(12.dp)
                                                                        ),
                                                                    contentScale = ContentScale.FillBounds,
                                                                    model = it.urlToImage,
                                                                    contentDescription = ""
                                                                )
                                                                Spacer(Modifier.width(12.dp))
                                                                Column(
                                                                    Modifier.weight(1f)
                                                                ) {
                                                                    Text(
                                                                        it.title ?: "no title",
                                                                        overflow = TextOverflow.Ellipsis,
                                                                        maxLines = 2,
                                                                        style = TextStyle(
                                                                            fontSize = 20.sp,
                                                                            fontWeight = FontWeight.Bold
                                                                        )
                                                                    )
                                                                    Spacer(
                                                                        Modifier.height(
                                                                            4.dp
                                                                        )
                                                                    )
                                                                    Row(Modifier.fillMaxWidth()) {
                                                                        Text(
                                                                            it.publishedAt?.substring(
                                                                                0,
                                                                                endIndex = 10
                                                                            ) ?: "no date",
                                                                            maxLines = 1,
                                                                            style = TextStyle(
                                                                                color = Color.Gray
                                                                            )
                                                                        )
                                                                        Spacer(Modifier.width(12.dp))
                                                                        Text(
                                                                            it.author
                                                                                ?: "no author",
                                                                            maxLines = 1,
                                                                            style = TextStyle(
                                                                                color = Color.Gray
                                                                            )
                                                                        )
                                                                    }
                                                                }
                                                                Spacer(Modifier.width(12.dp))


                                                            }


                                                        }

                                                    } else {
                                                        Text("no articles saved")
                                                    }

                                                }
                                            }
                                        }
                                    }else{
                                        Text("No Saved Articles")
                                        }
                                }
                            }else{
                                CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally), color = myGreen, backgroundColor = myGray)
                            }
                        }
                    }
                }
            }
        }
    }


}