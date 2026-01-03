package com.quotemaster.quotemasterapp.presentation.quoteEditor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quotemaster.quotemasterapp.data.local.entity.QuoteEntity
import com.quotemaster.quotemasterapp.utils.getQuoteBackgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddQuoteScreen(
    modifier: Modifier = Modifier,
    onQuoteSelected: (String, String) -> Unit,
    onDismiss: () -> Unit,
    categoryName: String
) {
    var quoteContent by remember { mutableStateOf("") }
    var quoteAuthor by remember { mutableStateOf("") }
    var showSelectQuoteScreen by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    if (showSelectQuoteScreen) {
        SelectQuoteScreen(
            modifier = modifier,
            onQuoteSelected = { quote ->
                quoteContent = quote.content
                quoteAuthor = quote.author?:""
                showSelectQuoteScreen = false
            },
            categoryName = categoryName,
            onDismiss = { showSelectQuoteScreen = false }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Add Quote",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                if (quoteContent.isNotEmpty() && quoteAuthor.isNotEmpty()) {
                                    onQuoteSelected(quoteContent, quoteAuthor)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Save",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF2C3E50)
                    )
                )
            },
            containerColor = Color.White
        ) { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Quote content input
                OutlinedTextField(
                    value = quoteContent,
                    onValueChange = { quoteContent = it },
                    label = { Text("Write or Paste Quote here") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2C3E50),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF2C3E50),
                        unfocusedLabelColor = Color.Gray
                    ),
                    maxLines = 8
                )

                // Author input
                OutlinedTextField(
                    value = quoteAuthor,
                    onValueChange = { quoteAuthor = it },
                    label = { Text("Author / Who Said!") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2C3E50),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF2C3E50),
                        unfocusedLabelColor = Color.Gray
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.weight(1f))

                // Save button
                Button(
                    onClick = {
                        if (quoteContent.isNotEmpty() && quoteAuthor.isNotEmpty()) {
                            onQuoteSelected(quoteContent, quoteAuthor)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00BCD4)
                    ),
                    enabled = quoteContent.isNotEmpty() && quoteAuthor.isNotEmpty()
                ) {
                    Text(
                        text = "SAVE",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Select Quote button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    FloatingActionButton(
                        onClick = { showSelectQuoteScreen = true },
                        containerColor = Color(0xFF2C3E50),
                        contentColor = Color.White,
                        modifier = Modifier.align(Alignment.BottomEnd)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Select Quote",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Select Quote",
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuoteItem(
    quote: QuoteEntity,
    onClick: () -> Unit,
    showCopyIcon: Boolean = false,
    onCopyClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = getQuoteBackgroundColor()
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = quote.content,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 24.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = if (showCopyIcon) 32.dp else 0.dp)
                    )

                    if (showCopyIcon) {
                        IconButton(
                            onClick = onCopyClick,
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = Color.Red.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                if (!quote.author.equals("unknown", ignoreCase = true)) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "- ${quote.author}",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic
                    )
                }

                if (quote.category.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = quote.category,
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}


