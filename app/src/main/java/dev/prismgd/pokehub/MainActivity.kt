package dev.prismgd.pokehub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

private val Background = Color(0xFF10091D)
private val Panel = Color(0xFF1D1230)
private val PanelAlt = Color(0xFF291B3D)
private val Purple = Color(0xFF9B5CFF)
private val Pink = Color(0xFFE45BCE)
private val Muted = Color(0xFFB9A9CC)

private data class CatalogEntry(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val platform: String,
    val symbol: String,
    val status: String,
    val details: String
)

private val catalog = listOf(
    CatalogEntry(
        "emerald-info", "Pokémon Emerald", "Informações e recursos para fãs",
        "Jogos", "Game Boy Advance", "🌿", "FICHA DEMONSTRATIVA",
        "Esta é uma ficha de demonstração. O PokéHub não fornece a ROM comercial. Use apenas cópias e materiais que você tenha direito legal de utilizar."
    ),
    CatalogEntry(
        "community-hack", "Projetos da comunidade", "ROM hacks e novas aventuras",
        "ROM Hacks", "Game Boy Advance", "🔥", "CATÁLOGO EM PREPARAÇÃO",
        "Espaço reservado para ROM hacks publicados por seus criadores, com autorização e instruções de aplicação de patches."
    ),
    CatalogEntry(
        "save-manager", "GBA Save Manager", "Organize seus arquivos de save",
        "Ferramentas", "Android", "💾", "EM BREVE",
        "Área reservada para ferramentas de gerenciamento de saves. Os recursos serão adicionados e testados antes de serem publicados."
    ),
    CatalogEntry(
        "map-toolkit", "Map Toolkit", "Ferramentas para criação de mapas",
        "Ferramentas", "PC / Android", "🧰", "EM BREVE",
        "Área reservada para ferramentas de desenvolvimento e edição de mapas para projetos compatíveis."
    ),
    CatalogEntry(
        "emulator-guide", "Emuladores compatíveis", "Guias e opções de emulação",
        "Emuladores", "Android", "🎮", "GUIA EM PREPARAÇÃO",
        "O PokéHub poderá catalogar emuladores e direcionar para suas páginas oficiais. Nenhum emulador é distribuído nesta versão."
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PokeHubApp() }
    }
}

@Composable
private fun PokeHubApp() {
    var search by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Todos") }
    var tab by remember { mutableStateOf(0) }
    var favorites by remember { mutableStateOf(setOf<String>()) }
    var selected by remember { mutableStateOf<CatalogEntry?>(null) }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Purple,
            secondary = Pink,
            background = Background,
            surface = Panel,
            onBackground = Color.White,
            onSurface = Color.White
        )
    ) {
        if (selected != null) {
            DetailScreen(
                entry = selected!!,
                isFavorite = favorites.contains(selected!!.id),
                onBack = { selected = null },
                onFavorite = {
                    favorites = if (favorites.contains(selected!!.id)) {
                        favorites - selected!!.id
                    } else {
                        favorites + selected!!.id
                    }
                }
            )
        } else {
            val categories = listOf("Todos", "Jogos", "ROM Hacks", "Emuladores", "Ferramentas")
            val visibleEntries = catalog.filter { entry ->
                val matchesCategory = category == "Todos" || entry.category == category
                val matchesSearch = entry.name.contains(search, true) ||
                    entry.description.contains(search, true) ||
                    entry.platform.contains(search, true)
                val matchesTab = tab != 2 || favorites.contains(entry.id)
                matchesCategory && matchesSearch && matchesTab
            }

            Scaffold(
                containerColor = Background,
                bottomBar = {
                    NavigationBar(containerColor = Color(0xFF190F28)) {
                        val labels = listOf("Início", "Explorar", "Biblioteca", "Perfil")
                        val icons = listOf("⌂", "⌕", "▣", "●")
                        labels.forEachIndexed { index, label ->
                            NavigationBarItem(
                                selected = tab == index,
                                onClick = { tab = index; selected = null },
                                icon = { Text(icons[index], fontSize = 20.sp) },
                                label = { Text(label, fontSize = 10.sp) }
                            )
                        }
                    }
                }
            ) { insets ->
                when (tab) {
                    3 -> ProfileScreen(Modifier.padding(insets))
                    else -> LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(insets),
                        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 28.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        item { Header() }
                        item {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(
                                    if (tab == 2) "Sua biblioteca" else if (tab == 1) "Explorar catálogo" else "O que vamos descobrir hoje?",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                SearchField(search = search, onSearch = { search = it })
                            }
                        }
                        if (tab != 2) {
                            item { FeaturedCard(onExplore = { tab = 1 }) }
                            item {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text("Categorias", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                    Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                                        categories.take(3).forEach { value ->
                                            FilterChip(
                                                selected = category == value,
                                                onClick = { category = value },
                                                label = { Text(value, fontSize = 11.sp) }
                                            )
                                        }
                                    }
                                    Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                                        categories.drop(3).forEach { value ->
                                            FilterChip(
                                                selected = category == value,
                                                onClick = { category = value },
                                                label = { Text(value, fontSize = 11.sp) }
                                            )
                                        }
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
                                Text(if (tab == 2) "Salvos" else "Catálogo", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                Text("${visibleEntries.size} ITENS", color = Purple, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        if (visibleEntries.isEmpty()) {
                            item {
                                EmptyState(
                                    if (tab == 2) "Sua biblioteca está vazia" else "Nenhum resultado encontrado",
                                    if (tab == 2) "Toque no coração de um item para guardá-lo aqui." else "Tente outro termo ou selecione outra categoria."
                                )
                            }
                        } else {
                            items(visibleEntries, key = { it.id }) { entry ->
                                CatalogCard(
                                    entry = entry,
                                    favorite = favorites.contains(entry.id),
                                    onOpen = { selected = entry },
                                    onFavorite = {
                                        favorites = if (favorites.contains(entry.id)) favorites - entry.id else favorites + entry.id
                                    }
                                )
                            }
                        }
                        item {
                            Text(
                                "PokéHub Alpha • Conteúdo demonstrativo",
                                color = Muted,
                                fontSize = 11.sp,
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                            )
                        }
                    }
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
                modifier = Modifier.size(46.dp).background(
                    Brush.linearGradient(listOf(Purple, Pink)),
                    RoundedCornerShape(15.dp)
                ),
                contentAlignment = Alignment.Center
            ) { Text("◉", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold) }
            Spacer(Modifier.width(11.dp))
            Column {
                Text("PokéHub", fontSize = 23.sp, fontWeight = FontWeight.ExtraBold)
                Text("POKÉMON COMMUNITY HUB", fontSize = 8.sp, color = Muted, letterSpacing = 1.2.sp)
            }
        }
        Surface(color = Panel, shape = CircleShape) {
            Text("✦", color = Pink, fontSize = 19.sp, modifier = Modifier.padding(11.dp))
        }
    }
}

@Composable
private fun SearchField(search: String, onSearch: (String) -> Unit) {
    OutlinedTextField(
        value = search,
        onValueChange = onSearch,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = { Text("Buscar jogos, hacks e ferramentas…", color = Muted) },
        leadingIcon = { Text("⌕", fontSize = 22.sp, color = Muted) },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Purple,
            unfocusedBorderColor = Color(0xFF49365F),
            focusedContainerColor = Panel,
            unfocusedContainerColor = Panel,
            cursorColor = Purple
        )
    )
}

@Composable
private fun FeaturedCard(onExplore: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onExplore),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Panel)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().background(
                Brush.linearGradient(listOf(Color(0xFF542A85), Color(0xFF28183F), Color(0xFF1B122B)))
            ).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("SEU HUB DE PROJETOS POKÉMON", color = Color(0xFFE6C8FF), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Text("Novas aventuras\ncomeçam aqui.", fontSize = 27.sp, lineHeight = 32.sp, fontWeight = FontWeight.ExtraBold)
            Text("Descubra criações, ferramentas e projetos da comunidade.", color = Color(0xFFE0D4EC), fontSize = 13.sp)
            Surface(color = Purple, shape = RoundedCornerShape(50)) {
                Text("EXPLORAR CATÁLOGO  →", modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun CatalogCard(
    entry: CatalogEntry,
    favorite: Boolean,
    onOpen: () -> Unit,
    onFavorite: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onOpen),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Panel)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(58.dp).background(PanelAlt, RoundedCornerShape(15.dp)),
                contentAlignment = Alignment.Center
            ) { Text(entry.symbol, fontSize = 28.sp) }
            Spacer(Modifier.width(13.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(entry.name, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(entry.description, color = Muted, fontSize = 11.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(entry.status, color = Purple, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            }
            Text(
                if (favorite) "♥" else "♡",
                color = if (favorite) Pink else Muted,
                fontSize = 24.sp,
                modifier = Modifier.clickable(onClick = onFavorite).padding(start = 10.dp, end = 2.dp)
            )
        }
    }
}

@Composable
private fun DetailScreen(
    entry: CatalogEntry,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onFavorite: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Background).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Text("←  Voltar", color = Purple, fontWeight = FontWeight.Bold, modifier = Modifier.clickable(onClick = onBack).padding(vertical = 8.dp))
        Box(
            modifier = Modifier.fillMaxWidth().height(180.dp).background(
                Brush.linearGradient(listOf(Color(0xFF542A85), Panel, Color(0xFF28183F))),
                RoundedCornerShape(24.dp)
            ),
            contentAlignment = Alignment.Center
        ) { Text(entry.symbol, fontSize = 76.sp) }
        Text(entry.category.uppercase(), color = Purple, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Text(entry.name, fontSize = 27.sp, fontWeight = FontWeight.ExtraBold)
        Text(entry.description, color = Muted)
        Surface(color = Panel, shape = RoundedCornerShape(14.dp)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Sobre este item", fontWeight = FontWeight.Bold)
                Text(entry.details, color = Muted, fontSize = 13.sp, lineHeight = 20.sp)
                Text("Plataforma: ${entry.platform}", color = Muted, fontSize = 12.sp)
                Text("Status: ${entry.status}", color = Purple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
        Button(
            onClick = onFavorite,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Purple),
            shape = RoundedCornerShape(14.dp)
        ) { Text(if (isFavorite) "♥  Remover da biblioteca" else "♡  Salvar na biblioteca") }
        Text("Downloads e instalação serão habilitados quando o catálogo e os repositórios estiverem prontos.", color = Muted, fontSize = 11.sp)
    }
}

@Composable
private fun EmptyState(title: String, message: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 35.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("◇", color = Purple, fontSize = 42.sp)
        Text(title, fontWeight = FontWeight.Bold)
        Text(message, color = Muted, fontSize = 12.sp)
    }
}

@Composable
private fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().background(Background).padding(22.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Header()
        Spacer(Modifier.height(12.dp))
        Text("Seu PokéHub", fontSize = 27.sp, fontWeight = FontWeight.ExtraBold)
        Text("Perfil local", color = Purple, fontWeight = FontWeight.Bold)
        Surface(color = Panel, shape = RoundedCornerShape(18.dp)) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("PokéHub Alpha 0.1.0", fontWeight = FontWeight.Bold)
                Text("Marketplace comunitário em desenvolvimento.", color = Muted, fontSize = 13.sp)
                Text("Conta, sincronização e publicação de projetos serão adicionadas em etapas futuras.", color = Muted, fontSize = 12.sp)
            }
        }
        Text("Feito para a comunidade Pokémon.", color = Muted, fontSize = 12.sp)
    }
}
