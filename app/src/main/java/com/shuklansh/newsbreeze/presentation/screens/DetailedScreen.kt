package com.shuklansh.newsbreeze.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.size
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import coil.compose.AsyncImage
import com.shuklansh.newsbreeze.R
import com.shuklansh.newsbreeze.domain.data.Article
import com.shuklansh.newsbreeze.presentation.NewsViewModel
import com.shuklansh.newsbreeze.presentation.user_events.UserEvent
import com.shuklansh.newsbreeze.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import topAppBar

@AndroidEntryPoint
class DetailedScreen : Fragment() {

    private var title: String = ""
    private var content: String = ""
    private var savedBool: Boolean = false
    private var publishedAt: String = ""
    private var image: String = ""
    private var author: String = ""
    private var description: String = ""
    private var url: String = ""

    var isarticleinDB = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString("title")?.let {
            title = it
        }
        arguments?.getString("content")?.let {
            content = it
        }

        arguments?.getBoolean("savedBool")?.let {
            savedBool = it
            isarticleinDB = savedBool

        }
        arguments?.getString("publishedAt")?.let {
            publishedAt = it
        }
        arguments?.getString("image")?.let {
            image = it
        }
        arguments?.getString("author")?.let {
            author = it
        }
        arguments?.getString("description")?.let {
            description = it
        }
        arguments?.getString("url")?.let {
            url = it
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return ComposeView(requireContext()).apply {
            setContent {
                val vm: NewsViewModel by viewModels()
                val scope = rememberCoroutineScope()
                var articFromPrevScreen = Article(
                    title = title,
                    content = content,
                    publishedAt = publishedAt,
                    urlToImage = image,
                    author = author,
                    description = description,
                    url = url
                )

                NewsBreezeTheme {
                    Scaffold(Modifier.fillMaxSize()) {
                        var bookmarked by remember {
                            mutableStateOf(isarticleinDB)
                        }
//                        LaunchedEffect(key1 = bookmarked ){
////                            isarticleinDB = vm.isarticleindb(articFromPrevScreen)
//                            isarticleinDB = savedBool
//                            bookmarked = isarticleinDB
//                        }

                        //topBar = {topAppBar(dash = false, saved = false,nav = findNavController() )}) {
                        Box(Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                            ) {
                                AsyncImage(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.5f),
                                    contentScale = ContentScale.FillBounds,
                                    model = image,
                                    contentDescription = ""
                                )
                                Column(
                                    Modifier.fillMaxWidth().fillMaxHeight(0.4f).padding(start = 16.dp, end = 8.dp, bottom = 16.dp),
                                    verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        textAlign = TextAlign.Start,
                                        text = publishedAt,
                                        color = myLightGray,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.W300
                                    )

                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    title,
                                    maxLines = 3,
                                    overflow = TextOverflow.Clip,
                                    color = myAppBg,
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.W600
                                )
                                }
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(start = 12.dp, end = 12.dp, top = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    IconButton(onClick = {
                                        findNavController().popBackStack(
                                            destinationId = R.id.dashboardScreen,
                                            inclusive = false
                                        )
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBackIos,
                                            contentDescription = "", tint = myAppBg
                                        )
                                    }

                                    if (bookmarked) {
                                        IconButton(onClick = {
                                            scope.launch {
                                                //vm.removeFromDb(article = articFromPrevScreen)
                                                vm.onEvent(UserEvent.removeBookmarkArticle(articFromPrevScreen))
                                                vm.onEvent(UserEvent.GetBookmarkArticlebyOrderSave)
                                                isarticleinDB = false
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
                                            scope.launch {
                                                //vm.addtheArticleToDb(article = articFromPrevScreen)
                                                vm.onEvent(UserEvent.BookmarkArticle(article = articFromPrevScreen))
                                                vm.onEvent(UserEvent.GetBookmarkArticlebyOrderSave)
                                                isarticleinDB = true
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

//                                    IconButton(onClick = {  }) {
//                                        Icon(
//                                            painter = painterResource(
//                                                id = if (savedBool) {
//                                                    R.drawable.bookmark_fill
//                                                } else {
//                                                    R.drawable.bookmark
//                                                }
//                                            ), "", tint = myAppBg
//                                        )
//                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(),
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    Column(
                                        Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Bottom
                                    ) {


                                        Column(
                                            Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(0.6f)
                                                .clip(
                                                    RoundedCornerShape(
                                                        topStart = 24.dp,
                                                        topEnd = 24.dp
                                                    )
                                                )
                                                .background(myAppBg),
                                            horizontalAlignment = Alignment.Start,
                                            verticalArrangement = Arrangement.Top

                                        ) {
                                            Column(
                                                Modifier
                                                    .padding(16.dp)
                                                    .verticalScroll(
                                                        rememberScrollState()
                                                    ),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Top
                                            ) {
                                                Spacer(modifier = Modifier.height(12.dp))
//                                                Row(
//                                                    Modifier.fillMaxWidth(),
//                                                    horizontalArrangement = Arrangement.Start
//                                                ) {
//                                                    Text(
//                                                        textAlign = TextAlign.Start,
//                                                        text = publishedAt,
//                                                        color = myGray,
//                                                        fontSize = 16.sp,
//                                                        fontWeight = FontWeight.W300
//                                                    )
//                                                }
//                                                Spacer(modifier = Modifier.height(12.dp))
//                                                Text(
//                                                    title,
//                                                    color = Color.Black,
//                                                    fontSize = 26.sp,
//                                                    fontWeight = FontWeight.W600
//                                                )
                                                Spacer(modifier = Modifier.height(12.dp))
                                                Text(
                                                    if (content == "null") {
                                                        "no content"
                                                    } else {
                                                        if(content.contains("chars]")){
                                                            content.replace(
                                                                content.slice(
                                                                    IntRange(
                                                                        content.indexOf(
                                                                            "["
                                                                        ), content.indexOf("]")
                                                                    )
                                                                ), ""
                                                            )
                                                        }else{
                                                            content
                                                        }
                                                    },
                                                    color = Color.Black,
                                                    fontSize = 20.sp,
                                                    fontWeight = FontWeight.W300
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
        }
    }

}