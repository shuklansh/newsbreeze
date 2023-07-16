package com.shuklansh.newsbreeze.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.size
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import bookmarkIcon
import coil.compose.AsyncImage
import com.shuklansh.newsbreeze.R
import com.shuklansh.newsbreeze.presentation.NewsViewModel
import com.shuklansh.newsbreeze.presentation.user_events.UserEvent
import com.shuklansh.newsbreeze.presentation.utils.UiEvent
import com.shuklansh.newsbreeze.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import myDivider
import searchBar
import topAppBar
import kotlin.properties.Delegates

@AndroidEntryPoint
class DashboardScreen : Fragment() {

    var isarticleinDB = false

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return ComposeView(requireContext()).apply {
            setContent {

                val vm: NewsViewModel by viewModels()
                val nav: NavController = findNavController()
                val query = vm.query.collectAsState().value
                val newsState = vm.newsList.collectAsState().value
                var scaffoldState = rememberScaffoldState()
                val scope = rememberCoroutineScope()

                LaunchedEffect(key1 = true) {

                    //vm.getNews("general")
                    vm.onEvent(UserEvent.getNewsByCategory("general"))
                    vm.eventFlow.collectLatest { event ->
                        when (event) {
                            is UiEvent.snackBarMessage -> {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    event.message
                                )
                            }
                        }
                    }

                }

                NewsBreezeTheme {
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize(),
                        topBar = {
                            topAppBar(dash = true, saved = false, detailed = false, nav = nav )
                        }


                    ) {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colors.primary)
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Column(
                                Modifier
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Top
                            ) {
                                searchBar(queryEntered = query, vm = vm)
                                Spacer(modifier = Modifier.fillMaxHeight(0.06f))
                                if (!newsState.isLoading) {
                                    LazyColumn {
                                        items(newsState.newsArticles) {
                                            var bookmarked by remember {
                                                mutableStateOf(isarticleinDB)
                                            }
                                            LaunchedEffect(key1 = bookmarked ){
                                                isarticleinDB = vm.isarticleindb(it)
                                                bookmarked = isarticleinDB
                                            }

                                            Column(
                                                Modifier
                                                    .fillMaxSize()
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .padding(8.dp),
                                                verticalArrangement = Arrangement.Bottom
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .padding(horizontal = 8.dp)
                                                        .fillMaxWidth()
                                                        .fillMaxHeight()
                                                ) {
                                                    AsyncImage(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .height(240.dp)
                                                            .clip(
                                                                RoundedCornerShape(12.dp)
                                                            ),
                                                        contentDescription = "",
                                                        model = it.urlToImage,
                                                        placeholder = painterResource(id = R.drawable.placeholder),
                                                        contentScale = ContentScale.FillBounds
                                                    )
                                                    Row(
                                                        Modifier
                                                            .fillMaxWidth()
                                                            .padding(end = 12.dp, top = 12.dp)
                                                            .fillMaxHeight(0.3f),
                                                        horizontalArrangement = Arrangement.End,
                                                        verticalAlignment = Alignment.Top
                                                    ) {

                                                        Box(
                                                            modifier = Modifier
                                                                .width(52.dp)
                                                                .height(52.dp)
                                                                .clip(
                                                                    RoundedCornerShape(8.dp)
                                                                )
                                                                .background(
                                                                    if (bookmarked) {
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


                                                                    if (bookmarked) {
                                                                        IconButton(onClick = {
                                                                            scope.launch{
                                                                                //vm.removeFromDb(article = it )
                                                                                vm.onEvent(UserEvent.removeBookmarkArticle(it))
                                                                                vm.onEvent(UserEvent.GetBookmarkArticlebyOrderSave)
                                                                                isarticleinDB = vm.isarticleindb(it)
                                                                                bookmarked = isarticleinDB
                                                                            }

                                                                        }) {
                                                                            Icon(
                                                                                modifier = Modifier.size(32.dp),
                                                                                tint = myAppBg,
                                                                                painter = painterResource(id = R.drawable.bookmark_fill),
                                                                                contentDescription = "unSave"
                                                                            )
                                                                        }
                                                                    } else {
                                                                        IconButton(onClick = {
                                                                            scope.launch{
                                                                                vm.onEvent(UserEvent.BookmarkArticle(it))
                                                                                vm.onEvent(UserEvent.GetBookmarkArticlebyOrderSave)
                                                                                isarticleinDB = vm.isarticleindb(it)
                                                                                bookmarked = isarticleinDB
                                                                            }
                                                                        }) {
                                                                            Icon(
                                                                                modifier = Modifier.size(32.dp),
                                                                                tint = myAppBg,
                                                                                painter = painterResource(id = R.drawable.bookmark),
                                                                                contentDescription = "save"
                                                                            )
                                                                        }

                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                Spacer(modifier = Modifier.height(8.dp))
                                                Column(
                                                    Modifier
                                                        .fillMaxSize()
                                                        .padding(horizontal = 8.dp)
                                                        .clip(RoundedCornerShape(12.dp)),
                                                    horizontalAlignment = Alignment.Start,
                                                    verticalArrangement = Arrangement.Bottom
                                                ) {

                                                    Spacer(modifier = Modifier.height(12.dp))
                                                    Text(
                                                        fontSize = 28.sp,
                                                        text = it.title ?: "no title",
                                                        fontWeight = FontWeight.W800,
                                                        maxLines = 2,
                                                        overflow = TextOverflow.Clip
                                                    )
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    Text(
                                                        fontSize = 16.sp,
                                                        text = it.content ?: "no content",
                                                        overflow = TextOverflow.Ellipsis,
                                                        maxLines = 3
                                                    )
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    Text(
                                                        text = it.publishedAt?.substring(
                                                            IntRange(
                                                                start = 0,
                                                                endInclusive = 9
                                                            )
                                                        )
                                                            ?: "no publisher",
                                                        color = myGray,
                                                        overflow = TextOverflow.Ellipsis,
                                                        maxLines = 1
                                                    )
                                                    Spacer(modifier = Modifier.height(12.dp))
                                                    Row(
                                                        Modifier
                                                            .fillMaxWidth()
                                                            .height(56.dp),
                                                        horizontalArrangement = Arrangement.Center,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Button(
                                                            modifier = Modifier
                                                                .padding(start = 12.dp, end = 12.dp)
                                                                .weight(1f),
                                                            shape = RoundedCornerShape(8.dp),
                                                            onClick = {
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
                                                                    bookmarked ?: false
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
                                                                findNavController().navigate(R.id.action_dashboardScreen_to_detailedScreen,bundle)
                                                          },
                                                            colors = ButtonDefaults.buttonColors(
                                                                backgroundColor = myGreen
                                                            )
                                                        ) {
                                                            Text(
                                                                text = "Read",
                                                                style = TextStyle(
                                                                    color = myAppBg
                                                                )
                                                            )
                                                        }
                                                        Spacer(modifier = Modifier.width(2.dp))
                                                        Button(
                                                            modifier = Modifier
                                                                .padding(start = 12.dp, end = 12.dp)
                                                                .weight(1f),
                                                            shape = RoundedCornerShape(8.dp),
                                                            onClick = {
                                                                scope.launch{
                                                                    vm.onEvent(UserEvent.BookmarkArticle(it))
                                                                    vm.onEvent(UserEvent.GetBookmarkArticlebyOrderSave)
                                                                    isarticleinDB = vm.isarticleindb(it)
                                                                    bookmarked = isarticleinDB
                                                                }
                                                          },
                                                            colors = ButtonDefaults.buttonColors(
                                                                backgroundColor = myGreen
                                                            )
                                                        ) {
                                                            Text(
                                                                text = "Save",
                                                                style = TextStyle(
                                                                    color = myAppBg
                                                                )
                                                            )
                                                        }
                                                    }
                                                    Spacer(modifier = Modifier.height(12.dp))
                                                    myDivider()
                                                    Spacer(modifier = Modifier.height(12.dp))


                                                }
                                            }

                                        }
                                    }
                                } else {
                                    CircularProgressIndicator(
                                        Modifier.align(Alignment.CenterHorizontally),
                                        color = myGreen
                                    )
                                }

                            }

                        }
                    }
                }
            }
        }


    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NewsBreezeTheme {
    }
}


//                        Text(
//                            modifier = Modifier.clickable { findNavController().navigate(R.id.action_dashboardScreen_to_savedScreen) },
//                            text = "Dashboard Screen to save ", style = TextStyle(
//                            color = MaterialTheme.colors.onPrimary
//                        ))
//                        Text(
//                            modifier = Modifier.clickable { findNavController().navigate(R.id.action_dashboardScreen_to_detailedScreen) },
//                            text = "Dashboard Screen to detailed ", style = TextStyle(
//                                color = MaterialTheme.colors.onPrimary
//                            ))



