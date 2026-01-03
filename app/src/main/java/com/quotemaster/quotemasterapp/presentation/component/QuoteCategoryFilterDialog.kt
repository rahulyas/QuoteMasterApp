package com.quotemaster.quotemasterapp.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quotemaster.quotemasterapp.utils.QuoteCategory

@Composable
fun QuoteCategoryFilterDialog(
    currentSelection: QuoteCategory,
    onCategorySelected: (QuoteCategory) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {
            Text(
                text = "Keywords",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            LazyColumn {
                items(QuoteCategory.entries.toTypedArray()) { category ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = category == currentSelection,
                                onClick = {
                                    onCategorySelected(category)
                                    onDismiss() // Optional: auto-dismiss on select
                                }
                            )
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = category == currentSelection,
                            onClick = {
                                onCategorySelected(category)
                                onDismiss() // Optional: auto-dismiss on select
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = category.displayName)
                    }
                }
            }
        },
        shape = RoundedCornerShape(12.dp)
    )
}
