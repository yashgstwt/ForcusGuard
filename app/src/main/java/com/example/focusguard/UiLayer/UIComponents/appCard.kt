package com.example.focusguard.UiLayer.UIComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp



@Composable
fun AppCard( appName: String,  appIcon: ImageBitmap , duration:String) {
    Row (
        modifier = Modifier
            .padding(10.dp)
            .shadow(elevation = 25.dp, shape = RoundedCornerShape(25.dp) ,  ambientColor = Color.White,spotColor = Color.White )
            .fillMaxWidth()
            .background(color = Color.Black)
            .padding(16.dp)

    ){
        Image(bitmap = appIcon , contentDescription = null , modifier = Modifier.size(50.dp))
        Text(text = appName, color = Color.White, modifier = Modifier.padding(10.dp))
        Text(text = duration, color = Color.White, modifier = Modifier.padding(10.dp))

    }
}


