package com.jalloft.lero.ui.screens.loggedin.registration.city

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jalloft.lero.R
import com.jalloft.lero.data.domain.City
import com.jalloft.lero.ui.components.SearchTextField
import com.valentinilk.shimmer.shimmer


@Composable
fun ChooseCityScreen(
    city: City?,
    citySearchViewModel: CityViewModel,
    onBack: () -> Unit,
    onSelectedCity: (City?) -> Unit,
) {
    var query by remember { mutableStateOf("") }

    var currentCity by remember { mutableStateOf(city) }

    var cityRemoved by remember { mutableStateOf(false) }

    BackHandler(true) { onBack() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(id = R.string.back)
                )
            }

            TextButton(
                onClick = {
                    onSelectedCity(currentCity)
                    query = ""
                    citySearchViewModel.setSearchQuery(query)
                },
                enabled = currentCity != null && currentCity?.osmId != city?.osmId || (cityRemoved && city != null)
            ) {
                Text(text = stringResource(R.string.save))
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.what_is_your_hometown),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            SearchTextField(
                value = query,
                onValueChange = {
                    query = it
                    citySearchViewModel.setSearchQuery(query)
                },
                modifier = Modifier.padding(top = 16.dp),
                placeholder = stringResource(R.string.search_city)
            )

            AnimatedVisibility(visible = currentCity != null && !citySearchViewModel.isSearchLoading) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        currentCity?.let {city ->
                            Text(
                                text = "${city.name.orEmpty()}, ${city.state.orEmpty()}, ${city.country.orEmpty()}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 2
                            )
                        }
                        IconButton(onClick = {
                            currentCity = null
                            cityRemoved = true
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = stringResource(R.string.remove_city)
                            )
                        }
                    }
                    Divider()
                }
            }

            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
                LazyColumn(
                    userScrollEnabled = !citySearchViewModel.isSearchLoading,
                    content = {
                        val places = citySearchViewModel.searchResults
                        if (places.isNotEmpty()) {
                            items(places.size) { index ->
                                val item = places[index]
                                Column(modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { currentCity = item.toCity }) {
                                    Text(
                                        text = "${item.name}, ${item.address?.state}, ${item.address?.country}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = item.displayName.orEmpty(),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onBackground.copy(.5f)
                                    )

                                }
                            }
                        } else if (citySearchViewModel.isSearchLoading) {
                            repeat(20) {
                                item {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 12.dp)
                                            .shimmer()
                                    ) {
                                        Divider(
                                            thickness = 15.dp,
                                            modifier = Modifier.width(200.dp)
                                        )
                                        Spacer(modifier = Modifier.size(8.dp))
                                        Divider(thickness = 24.dp)
                                    }

                                }
                            }
                        }

                    },
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                if (query.trim().isEmpty()) {
                    Image(
                        painter = painterResource(id = R.drawable.illustration_location_search),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                            .padding(horizontal = 16.dp)
                            .absolutePadding(bottom = 50.dp),
                        contentScale = ContentScale.FillWidth,
                    )
                }

            }

        }
    }

}
