package com.example.onlineshop.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.onlineshop.R
import com.example.project1762.Helper.ManagmentCart
import java.nio.file.WatchEvent

class CartActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
fun calculatorCart(managmentCart: ManagmentCart,tax : MutableState<Double>){
    val percentTax=0.02
    tax.value=Math.round((managmentCart.getTotalFee()*percentTax)*100)/100.0

}
@Composable
private fun CartScreen(
    managmentCart: ManagmentCart= ManagmentCart(LocalContext.current),
    onBackCLick:()->Unit
){
    val cartItems= remember{ mutableStateOf(managmentCart.getListCart()) }
    val tax=remember { mutableStateOf(0.0) }

    calculatorCart(managmentCart,tax)
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        ConstraintLayout (modifier = Modifier.padding(top=36.dp)){
            val (backbtn,cartTxt)=createRefs()
            Text(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(cartTxt){centerTo(parent)},
                text = "Your Cart",
                textAlign = TextAlign.Center,
                fontWeight= FontWeight.Bold,
                fontSize = 25.sp)
            Image(
                painter=painterResource(R.drawable.back),
                contentDescription = null,
                modifier = Modifier
                    .clickable{onBackCLick()}
                    .constrainAs(backbtn){
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
            )
        }
        if(cartItems.value.isEmpty){
            Text(text="Cart is Empty", modifier = Modifier.align (Alignment.CenterHorizontally))
        }
    }
}