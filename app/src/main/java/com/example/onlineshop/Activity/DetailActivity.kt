package com.example.onlineshop.Activity

import android.content.Intent
import android.os.Bundle
import com.example.onlineshop.R
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.example.onlineshop.Model.ItemsModel
import com.example.project1762.Helper.ManagmentCart
import androidx.compose.material3.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Button
class DetailActivity: BaseActivity() {
    private lateinit var item: ItemsModel
    private lateinit var manangementCart: ManagmentCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        item=intent.getSerializableExtra("object") as ItemsModel
        manangementCart= ManagmentCart(this)

        setContent {
            DetailScreen(
                item=item,
                onBackClick={finish()},
                onAddToCartClick={
                    item.numberInCart=1
                    manangementCart.insertItem(item)
                },
                onCartClick={
                    startActivity(Intent(this, CartActivity::class.java))
                }
            )
        }
    }

    @Composable
    private fun DetailScreen(
        item: ItemsModel,
        onBackClick: () -> Unit,
        onAddToCartClick: () -> Unit,
        onCartClick: () -> Unit,
    ) {
        var selectedImageUrl by remember { mutableStateOf(item.picUrl.first()) }
        var selectedModelIndex by remember { mutableStateOf(-1) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            ConstraintLayout (
                modifier = Modifier
                    .fillMaxSize()
                    .height(430.dp)
                    .padding(bottom = 16.dp)
            ){
                val (back,fav,mainImage,thumbnail)=createRefs()
                Image(
                    painter = rememberAsyncImagePainter(model=selectedImageUrl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(R.color.lightBrown),
                            shape= RoundedCornerShape(8.dp)
                        )
                        .constrainAs(mainImage){
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                        }
                )
                Image(
                    painter = painterResource(R.drawable.back),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(top=48.dp, start = 16.dp)
                        .clickable{onBackClick()}
                        .constrainAs(back){
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                )
                LazyRow(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .background(color=colorResource(R.color.white),
                            shape=RoundedCornerShape(18.dp))
                        .constrainAs(thumbnail){
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                        }
                ) {
                    items(item.picUrl){
                        imageUrl-> ImageThumbNail( imageUrl=imageUrl,
                        isSelected = selectedImageUrl==imageUrl,
                        onClick={selectedImageUrl=imageUrl})
                    }
                }
            }
            Row(verticalAlignment= Alignment.CenterVertically,
                modifier= Modifier.padding(top=16.dp)
                    .padding(horizontal = 16.dp))
            {
                Text(
                    text=item.title,
                    fontSize = 25.sp,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(end=16.dp)
                )
                Text(
                    text="$${item.price}",
                    fontSize = 22.sp
                )

            }
            RatingBar(rating=item.rating)

            ModelSelector(
                models=item.model,
                selectedModeIndex=selectedModelIndex,
                onModelSelected={selectedModelIndex=it}
            )
            Text(
                text=item.description,
                fontSize = 14.sp,
                color=Color.Black,
                modifier = Modifier.padding(16.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier=Modifier.fillMaxWidth().padding()
            ){
                IconButton(
                    onClick = onCartClick,
                    modifier = Modifier.background(colorResource(R.color.lightBrown),
                        shape = RoundedCornerShape(10.dp ))
                ){
                    Icon(
                        painter = painterResource(id=R.drawable.btn_2),
                        contentDescription = "Cart",
                        tint=Color.Black
                    )
                }
                Button(onClick = onAddToCartClick,
                    shape=RoundedCornerShape(10.dp),
                    colors= ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.darkBrown)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start=8.dp)
                        .height(50.dp)
                ) {
                    Text(
                        text="Add to cart",
                        fontSize = 18.sp,
                        color=Color.White
                    )
                }
            }
        }
    }

    @Composable
    fun ModelSelector(
        models: ArrayList<String>,
        selectedModeIndex: Int,
        onModelSelected: (Int) -> Unit
    ) {

        LazyRow (modifier= Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)){
            itemsIndexed(models){
                index,model->
                Box(modifier= Modifier
                    .padding(end=16.dp)
                    .height(40.dp)
                    .then(
                        if(index==selectedModeIndex){
                            Modifier.border(1.dp,colorResource(R.color.darkBrown),
                                RoundedCornerShape(10.dp))
                        }else{
                            Modifier.border(1.dp,colorResource(R.color.darkBrown),
                                RoundedCornerShape(10.dp))
                        }
                    )
                    .background(
                        if(index==selectedModeIndex)colorResource(R.color.darkBrown)else
                    colorResource(R.color.white),
                        shape = RoundedCornerShape(10.dp))
                    .clickable{onModelSelected(index)}
                    .padding(horizontal = 16.dp)
                ){
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = model,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = if (index == selectedModeIndex) colorResource(R.color.white)
                        else colorResource(R.color.black)
                    )
                }
            }
        }
    }

    @Composable
    fun RatingBar(rating: Double) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top=16.dp).padding(horizontal = 16.dp)
        ){
            Text(
                text="Select Model",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(id=R.drawable.star),
                contentDescription = null,
                modifier = Modifier.padding(end=8.dp)
            )
            Text(text="$rating Rating", style = MaterialTheme.typography.body2)
        }
    }

    @Composable
    fun ImageThumbNail(imageUrl: String, isSelected: Boolean, onClick: () ->Unit) {
        val backColr= if(isSelected)colorResource(R.color.darkBrown)else
            colorResource(R.color.veryLightBrown)

        Box(
            modifier= Modifier
                .padding(4.dp)
                .size(55.dp)
                .then(
                    if(isSelected){
                        Modifier.border(1.dp,colorResource(R.color.darkBrown),
                            RoundedCornerShape(10.dp))
                    }else{
                        Modifier
                    }
                )
                .background(backColr,shape= RoundedCornerShape(10.dp))
                .clickable(onClick=onClick)

        ){
            Image(
                painter=rememberAsyncImagePainter(model=imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}