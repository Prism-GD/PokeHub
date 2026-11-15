package dev.prismgd.pokehub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val HubBackground = Color(0xFF10091D)
private val HubSurface = Color(0xFF1D1230)
private val HubPurple = Color(0xFF9B5CFF)
private val HubPink = Color(0xFFE45BCE)
private val HubMuted = Color(0xFFB9A9CC)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PokeHubApp() }
    }
}

private data class HubItem(
    val title: String,
    val subtitle: String,
    val category: String,
    val tag: String,
    val symbol: String
)

private val demoItems = listOf(
    HubItem("Pokémon: Emerald", "Aventura • Game Boy Advance", "Jogos", "HOME BREW / DEMO", "🌿"),
    HubItem("FireRed: Community Edition", "Projeto da comunidade • GBA", "ROM Hacks", "PROJETO", "🔥"),
    HubItem("GBA Save Manager", "Ferramenta para gerenciar saves", "Ferramentas", "UTILITÁRIO", "💾"),
    HubItem("PokéMap Toolkit", "Ferramentas para criação de mapas", "Ferramentas", "DEV TOOL", "🧰")
)

@Composable
private fun PokeHubApp() {
    var search by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todos") }
    var selectedTab by remember { mutableIntStateOf(0) }

    val categories = listOf("Todos", "Jogos", "ROM Hacks", "Emuladores", "Ferramentas")
    val filteredItems = demoItems.filter {
        (selectedCategory == "Todos" || it.category == selectedCategory) &&
            (it.title.contains(search, ignoreCase = true) ||
                it.subtitle.contains(search, ignoreCase = true))
    }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = HubPurple,
            secondary = HubPink,
            background = HubBackground,
            surface = HubSurface,
            onBackground = Color.White,
            onSurface = Color.White
        )
    ) {
        Scaffold(
            containerColor = HubBackground,
            bottomBar = {
                NavigationBar(containerColor = Color(0xFF190F28)) {
                    val tabs = listOf("Início", "Explorar", "Biblioteca", "Perfil")
                    val symbols = listOf("⌂", "⌕", "▣", "●")
                    tabs.forEachIndexed { index, label ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            icon = { Text(symbols[index], fontSize = 20.sp) },
                            label = { Text(label, fontSize = 10.sp) }
                        )
                    }
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    start = 20.dp, end = 20.dp, top = 22.dp, bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item {
                    Header()
                }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            "O que vamos jogar hoje?",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedTextField(
                            value = search,
                            onValueChange = { search = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            placeholder = { Text("Buscar jogos, hacks e ferramentas…", color = HubMuted) },
                            leadingIcon = { Text("⌕", fontSize = 22.sp, color = HubMuted) },
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HubPurple,
                                unfocusedBorderColor = Color(0xFF49365F),
                                focusedContainerColor = HubSurface,
                                unfocusedContainerColor = HubSurface,
                                cursorColor = HubPurple
                            )
                        )
                    }
                }
                item {
                    FeaturedCard()
                }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Categorias", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        androidx.compose.foundation.lazy.LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(categories) { category ->
                                FilterChip(
                                    selected = selectedCategory == category,
                                    onClick = { selectedCategory = category },
                                    label = { Text(category) }
                                )
                            }
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Descobrir", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("COMUNIDADE", color = HubPurple, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                if (filteredItems.isEmpty()) {
                    item {
                        Text(
                            "Nenhum resultado encontrado. Tente outra busca ou categoria.",
                            color = HubMuted,
                            modifier = Modifier.padding(vertical = 20.dp)
                        )
                    }
                } else {
                    items(filteredItems) { item ->
                        CatalogCard(item)
                    }
                }
                item {
                    Text(
                        "PokéHub • Um espaço feito pela comunidade",
                        color = HubMuted,
                        fontSize = 11.sp,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        Brush.linearGradient(listOf(HubPurple, HubPink)),
                        RoundedCornerShape(15.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("◉", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(11.dp))
            Column {
                Text("PokéHub", fontSize = 23.sp, fontWeight = FontWeight.ExtraBold)
                Text("POKÉMON COMMUNITY HUB", fontSize = 8.sp, color = HubMuted, letterSpacing = 1.2.sp)
            }
        }
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(HubSurface, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("♡", color = HubPink, fontSize = 23.sp)
        }
    }
}

@Composable
private fun FeaturedCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = HubSurface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF542A85), Color(0xFF28183F), Color(0xFF1B122B))
                    )
                )
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                Text("DESTAQUE DA COMUNIDADE", color = Color(0xFFE6C8FF), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text("Novas aventuras
começam aqui.", fontSize = 27.sp, lineHeight = 32.sp, fontWeight = FontWeight.ExtraBold)
                Text("Descubra projetos, ferramentas e criações de fãs.", color = Color(0xFFE0D4EC), fontSize = 13.sp)
                Surface(color = HubPurple, shape = RoundedCornerShape(50)) {
                    Text("EXPLORAR CATÁLOGO  →", modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun CatalogCard(item: HubItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = HubSurface)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(Color(0xFF302044), RoundedCornerShape(15.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(item.symbol, fontSize = 28.sp)
            }
            Spacer(Modifier.width(13.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(item.title, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(item.subtitle, color = HubMuted, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(item.tag, color = HubPurple, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.7.sp)
            }
            Text("›", color = HubMuted, fontSize = 28.sp)
        }
    }
}
