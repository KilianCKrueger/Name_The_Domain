package com.example.namethedomain

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.namethedomain.ui.theme.NameTheDomainTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NameTheDomainTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Layout()
                }
            }
        }
    }
}

enum class GameModes{
    Menu, FourAnswers, Typing, Help
}

enum class QuestionTypes{
    Domains, Capitals
}

enum class DifficultyModes{
    Easy, Hard
}


// val initialCountries = listOf("Åland","Albania","American Samoa","Andorra","Argentina","Australia","Austria","Bangladesh","Belgium","Bhutan","Bolivia","Botswana","Brazil","British Indian Ocean Territory","Bulgaria","Cambodia","Canada","Chile","China, People’s Republic of","Christmas Island","Colombia","Costa Rica","Croatia (Hrvatska)","Curaçao","Czech Republic (Czechia)","Denmark","Dominican Republic","Ecuador","Estonia (stands for Eesti)","Eswatini (formerly Swaziland)","European Union","Faroe Islands (stands for Føroyar)","Finland","France","Germany	(stands for Deutschland)","Ghana","Gibraltar","Greece","Greenland","Guam","Guatemala","Guernsey","Hong Kong (special administrative region of China)","Hungary","Iceland (stands for Ísland)","India","Indonesia","Ireland","Isle of Man","Israel","Italy","Japan","Jersey","Jordan","Kazakhstan","Kenya","Kyrgyzstan","Laos","Latvia","Lesotho","Liechtenstein","Lithuania","Luxembourg","Macau (Macao, special administrative region of China)","Madagascar","Malaysia","Maldives","Malta","Mexico","Monaco","Mongolia","Montenegro","Namibia","Nepal","Netherlands","New Zealand","Nigeria","North Macedonia	(stands for Македонија -> Makedonija)","Norway","Oman","Palestine (incl. Gaza, West Bank)","Panama","Peru","Philippines","Pitcairn Islands","Poland","Portugal","Puerto Rico","Qatar","Réunion (French overseas department)","Romania","Russia","Rwanda","Saint Helena (British overseas department)","Senegal","Serbia (stands for Република Србија -> Republika Srbija)","Singapore","Slovakia","Slovenia","South Africa (stands for Zuid-Afrika)","South Korea","Spain (stands for España)","Sri Lanka","Sweden","Switzerland","Taiwan","Thailand","Tokelau","Tunisia","Turkey","Tuvalu","Ukraine (stands for Україна -> Ukraina)","United Arab Emirates (UAE)","United Kingdom","United States of America","Uruguay","Vietnam","Yemen")
val sortedShortCountriesA = listOf("Åland","Albania","American Samoa","Andorra","Argentina","Australia","Austria","Bangladesh","Belgium","Bhutan","Bolivia","Botswana","Brazil","British Indian Ocean Territory","Bulgaria","Cambodia","Canada","Chile","China","Christmas Island","Colombia","Costa Rica","Croatia","Curaçao","Czech Republic","Denmark","Dominican Republic","Ecuador","Estonia","Eswatini","European Union","Faroe Islands","Finland","France","Germany","Ghana","Gibraltar","Greece","Greenland","Guam","Guatemala","Guernsey","Hong Kong","Hungary","Iceland","India","Indonesia","Ireland","Isle of Man","Israel","Italy","Japan","Jersey","Jordan","Kazakhstan","Kenya","Kyrgyzstan","Laos","Latvia","Lesotho","Liechtenstein","Lithuania","Luxembourg","Macau","Madagascar","Malaysia","Maldives","Malta","Mexico","Monaco","Mongolia","Montenegro","Namibia","Nepal","Netherlands","New Zealand","Nigeria","North Macedonia","Norway","Oman","Palestine","Panama","Peru","Philippines","Pitcairn Islands","Poland","Portugal","Puerto Rico","Qatar","Réunion","Romania","Russia","Rwanda","Saint Helena","Senegal","Serbia","Singapore","Slovakia","Slovenia","South Africa","South Korea","Spain","Sri Lanka","Sweden","Switzerland","Taiwan","Thailand","Tokelau","Tunisia","Turkey","Tuvalu","Uganda","Ukraine","United Arabian Emirates","United Kingdom","United States of America","Uruguay","Vietnam","Yemen")
val unsortedDomainsA = listOf(".ax",".al",".as",".ad",".ar",".au",".at",".bd",".be",".bt",".bo",".bw",".br",".io",".bg",".kh",".ca",".cl",".cn",".cx",".co",".cr",".hr",".cw",".cz",".dk",".do",".ec",".ee",".sz",".eu",".fo",".fi",".fr",".de",".gh",".gi",".gr",".gl",".gu",".gt",".gg",".hk",".hu",".is",".in",".id",".ie",".im",".il",".it",".jp",".je",".jo",".kz",".ke",".kg",".la",".lv",".ls",".li",".lt",".lu",".mo",".mg",".my",".mv",".mt",".mx",".mc",".mn",".me",".na",".np",".nl",".nz",".ng",".mk",".no",".om",".ps",".pa",".pe",".ph",".pn",".pl",".pt",".pr",".qa",".re",".ro",".ru",".rw",".sh",".sn",".rs",".sg",".sk",".si",".za",".kr",".es",".lk",".se",".ch",".tw",".th",".tk",".tn",".tr",".tv",".ug",".ua",".ae",".uk",".us",".uy",".vn",".ye")

val unsortedDomainCountriesB = listOf("Andorra", "United Arab Emirates", "Albania", "Argentina", "American Samoa", "Austria", "Australia", "Åland Islands", "Bangladesh", "Belgium", "Bulgaria", "Bahrain", "Bolivia", "Brazil", "Bhutan", "Botswana", "Belarus", "Canada", "Switzerland", "Chile", "China", "Colombia", "Costa Rica", "Curaçao", "Czech Republic", "Germany", "Denmark", "Dominican Republic", "Algeria", "Ecuador", "Estonia", "Spain", "European Union", "Finland", "Faroe Islands", "France", "Ghana", "Gibraltar", "Greenland", "Greece", "Guatemala", "Guam", "Hong Kong", "Croatia", "Hungary", "Indonesia", "Ireland", "Israel", "Isle of Man", "India", "British Indian Ocean Territory", "Iceland", "Italy", "Jersey", "Jordan", "Japan", "Kenya", "Kyrgyzstan", "Cambodia", "South Korea", "Kazakhstan", "Laos", "Liechtenstein", "Sri Lanka", "Lesotho", "Lithuania", "Luxembourg", "Latvia", "Libya", "Morocco", "Monaco", "Montenegro", "Madagascar", "North Macedonia", "Mongolia", "Macau", "Malta", "Maldives", "Mexico", "Malaysia", "Namibia", "Niger", "Nigeria", "Netherlands", "Norway", "Nepal", "New Zealand", "Oman", "Panama", "Peru", "Philippines", "Poland", "Pitcairn Islands", "Puerto Rico", "Palestine", "Portugal", "Qatar", "Réunion", "Romania", "Serbia", "Russia", "Rwanda", "Sweden", "Singapore", "Saint Helena, Ascension and Tristan da Cunha", "Slovenia", "Slovakia", "Senegal", "Suriname", "Sao Tome and Principe", "Eswatini", "Thailand", "Tajikistan", "Tokelau", "Tunisia", "Tonga", "Turkey", "Trinidad and Tobago", "Tuvalu", "Taiwan", "Tanzania", "Ukraine", "Uganda", "United Kingdom", "United States of America", "Uruguay", "Uzbekistan", "Vietnam", "Yemen", "South Africa")
val sortedDomainsB = listOf(".ad", ".ae", ".al", ".ar", ".as", ".at", ".au", ".ax", ".bd", ".be", ".bg", ".bh", ".bo", ".br", ".bt", ".bw", ".by", ".ca", ".ch", ".cl", ".cn", ".co", ".cr", ".cw", ".cz", ".de", ".dk", ".do", ".dz", ".ec", ".ee", ".es", ".eu", ".fi", ".fo", ".fr", ".gh", ".gi", ".gl", ".gr", ".gt", ".gu", ".hk", ".hr", ".hu", ".id", ".ie", ".il", ".im", ".in", ".io", ".is", ".it", ".je", ".jo", ".jp", ".ke", ".kg", ".kh", ".kr", ".kz", ".la", ".li", ".lk", ".ls", ".lt", ".lu", ".lv", ".ly", ".ma", ".mc", ".me", ".mg", ".mk", ".mn", ".mo", ".mt", ".mv", ".mx", ".my", ".na", ".ne", ".ng", ".nl", ".no", ".np", ".nz", ".om", ".pa", ".pe", ".ph", ".pl", ".pn", ".pr", ".ps", ".pt", ".qa", ".re", ".ro", ".rs", ".ru", ".rw", ".se", ".sg", ".sh", ".si", ".sk", ".sn", ".sr", ".st", ".sz", ".th", ".tj", ".tk", ".tn", ".to", ".tr", ".tt", ".tv", ".tw", ".tz", ".ua", ".ug", ".uk", ".us", ".uy", ".uz", ".vn", ".ye", ".za")

val unsortedCapitalsC = listOf("Tirana","Andorra la Vella","Buenos Aires","Canberra","Vienna","Dhaka","Brussels","Thimphu","La Paz (administrative), Sucre (official)","Gaborone","Brasilia","Sofia","Phnom Penh","Ottawa","Santiago","Beijing","Bogota","San Jose","Zagreb","Prague","Copenhagen","Santo Domingo","Quito","Tallinn","Mbabane","Helsinki","Paris","Berlin","Accra","Athens","Guatemala City","Budapest","Reykjavik","New Delhi","Jakarta","Dublin","Rome","Tokyo","Amman","Astana","Nairobi","Bishkek","Vientiane","Riga","Maseru","Vaduz","Vilnius","Luxembourg","Antananarivo","Kuala Lumpur","Male","Valletta","Mexico City","Monaco","Ulaanbaatar","Podgorica","Windhoek","Kathmandu","Amsterdam","Wellington","Abuja","Skopje","Oslo","Muscat","Panama City","Lima","Manila","Warsaw","Lisbon","San Juan","Doha","Bucharest","Moscow","Kigali","Dakar","Belgrade","Bratislava","Ljubljana","Pretoria, Bloemfontein, Cape Town","Seoul","Madrid","Sri Jayawardenapura Kotte","Stockholm","Bern","Taipei","Bangkok","Tunis","Ankara","Funafuti","Kampala","Kyiv","Abu Dhabi","London","Washington D.C.","Montevideo","Hanoi","Sana'a")
val sortedCapitalCountriesC = listOf("Albania","Andorra","Argentina","Australia","Austria","Bangladesh","Belgium","Bhutan","Bolivia","Botswana","Brazil","Bulgaria","Cambodia","Canada","Chile","China","Colombia","Costa Rica","Croatia","Czech Republic","Denmark","Dominican Republic","Ecuador","Estonia","Eswatini","Finland","France","Germany","Ghana","Greece","Guatemala","Hungary","Iceland","India","Indonesia","Ireland","Italy","Japan","Jordan","Kazakhstan","Kenya","Kyrgyzstan","Laos","Latvia","Lesotho","Liechtenstein","Lithuania","Luxembourg","Madagascar","Malaysia","Maldives","Malta","Mexico","Monaco","Mongolia","Montenegro","Namibia","Nepal","Netherlands","New Zealand","Nigeria","North Macedonia","Norway","Oman","Panama","Peru","Philippines","Poland","Portugal","Puerto Rico","Qatar","Romania","Russia","Rwanda","Senegal","Serbia","Slovakia","Slovenia","South Africa","South Korea","Spain","Sri Lanka","Sweden","Switzerland","Taiwan","Thailand","Tunisia","Turkey","Tuvalu","Uganda","Ukraine","United Arabian Emirates","United Kingdom","United States of America","Uruguay","Vietnam","Yemen")

val sortedCapitalsD = listOf("Abu Dhabi", "Abuja", "Accra", "Amman", "Amsterdam", "Ankara", "Antananarivo", "Andorra la Vella", "Astana", "Athens", "Bangkok", "Beijing", "Belgrade", "Berlin", "Bern", "Bishkek", "Bogotá", "Brasilia", "Bratislava", "Brussels", "Bucharest", "Budapest", "Buenos Aires", "Canberra", "Copenhagen", "Dakar", "Dhaka", "Doha", "Dublin", "Funafuti", "Gaborone", "Guatemala City", "Hanoi", "Helsinki", "Jakarta", "Kampala", "Kigali", "Kathmandu", "Kuala Lumpur", "Kyiv", "La Paz (administrative), Sucre (official)", "Lima", "Lisbon", "Ljubljana", "London", "Luxembourg", "Madrid", "Male", "Manila", "Maseru", "Mbabane", "Mexico City", "Monaco", "Montevideo", "Moscow", "Muscat", "Nairobi", "New Delhi", "Oslo", "Ottawa", "Panama City", "Paris", "Phnom Penh", "Podgorica", "Prague", "Pretoria, Bloemfontein, Cape Town", "Quito", "Reykjavik", "Riga", "Rome", "San Jose", "San Juan", "Sana'a", "Santiago", "Santo Domingo", "Seoul", "Skopje", "Sofia", "Sri Jayawardenapura Kotte", "Stockholm", "Taipei", "Tallinn", "Thimphu", "Tirana", "Tokyo", "Tunis", "Tuvalu", "Ulaanbaatar", "Vaduz", "Valletta", "Vienna", "Vientiane", "Vilnius", "Warsaw", "Washington D.C.", "Wellington", "Windhoek", "Zagreb")
val unsortedCapitalCountriesD = listOf("United Arabian Emirates", "Nigeria", "Ghana", "Jordan", "Netherlands", "Turkey", "Madagascar", "Andorra", "Kazakhstan", "Greece", "Thailand", "China", "Serbia", "Germany", "Switzerland", "Kyrgyzstan", "Colombia", "Brazil", "Slovakia", "Belgium", "Romania", "Hungary", "Argentina", "Australia", "Denmark", "Senegal", "Bangladesh", "Qatar", "Ireland", "Tuvalu", "Botswana", "Guatemala", "Vietnam", "Finland", "Indonesia", "Uganda", "Rwanda", "Nepal", "Malaysia", "Ukraine", "Bolivia", "Peru", "Portugal", "Slovenia", "United Kingdom", "Luxembourg", "Spain", "Maldives", "Philippines", "Lesotho", "Eswatini", "Mexico", "Monaco", "Uruguay", "Russia", "Oman", "Kenya", "India", "Norway", "Canada", "Panama", "France", "Cambodia", "Montenegro", "Czech Republic", "South Africa", "Ecuador", "Iceland", "Latvia", "Italy", "Costa Rica", "Puerto Rico", "Yemen", "Chile", "Dominican Republic", "South Korea", "North Macedonia", "Bulgaria", "Sweden", "Sri Lanka", "Taiwan", "Estonia", "Bhutan", "Albania", "Japan", "Tunisia", "Tuvalu", "Mongolia", "Liechtenstein", "Malta", "Austria", "Laos", "Lithuania", "Poland", "United States of America", "New Zealand", "Namibia", "Croatia")

// default: question = capital || question = country (domains are answers)
var reverseAnswerAndQuestion = false


// default values at start
object GameMode{
    var globalGameMode by mutableStateOf(GameModes.Menu)
}

object QuestionType{
    var globalQuestionType by mutableStateOf(QuestionTypes.Domains)
}

object Difficulty{
    var globalDifficulty by mutableStateOf(DifficultyModes.Easy)
}

// -------------------

var questionList: List<String> = sortedCapitalsD
var answerList: List<String> = unsortedCapitalCountriesD

val mainPurple = Color(0xFF673AB7)
val lightPurple = Color(0xFF7846D3)
val yellow = Color(0xFFFFEB3B)
val textShadowPurple = Color(0xFF422677)
val gameModeButtonColor = Color(0xFF827EB2)
val textBeige = Color(0xFFFFFFF2)
val barYellow = Color(0xC1FFEB3B)
val greyText = Color(0xFF232323)
val blackText = Color(0xFF1A1919)
val greenCorrect = Color(0xB991DC34)


@Composable
fun Layout() {

    when (GameMode.globalGameMode) {
        GameModes.Menu -> {
            NavBar("Main")
        }
        GameModes.FourAnswers -> {
            NavBar("4 Answers")
        }
        GameModes.Typing -> {
            NavBar("Typing")
        }
        GameModes.Help -> {
            NavBar("Help")
        }
    }
}

// implements column that encloses everything, and a column for the content outside the the navbar (in the middle area)
@Composable
fun NavBar(pageTitle:String){

    // for Toast
    val context = LocalContext.current

    Column(

        modifier = Modifier
            .background(color = mainPurple)
            .padding(top = 4.dp, bottom = 0.dp, start = 6.dp, end = 6.dp)
    ) {
        // space for front camera
        Row(
            modifier = Modifier.padding(top = 40.dp)
        ) {}

        // Default Bar: Mode Globe Help
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(mainPurple)
                .height(75.dp)
        ) {
            Button(
                // to make text fill whole button
                contentPadding = PaddingValues(0.dp),
                content = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = pageTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = yellow,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = textShadowPurple,
                                    offset = Offset(10.0f, 10.0f),
                                    blurRadius = 3f,
                                )
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                },
                onClick = { if(GameMode.globalGameMode != GameModes.Menu){
                    GameMode.globalGameMode = GameModes.Menu
                } else {
                    Toast.makeText(context, "You are already in the main menu, silly ;D", Toast.LENGTH_SHORT).show()
                }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = mainPurple,
                    contentColor = yellow
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(75.dp)
            )

            Button(
                contentPadding = PaddingValues(0.dp),
                content = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painterResource(R.drawable.name_the_domain_logo),
                            "Name The Domain Logo",
                            Modifier
                                .size(75.dp)
                        )
                        Box(
                            Modifier
                                .size(75.dp)
                                .offset(10.dp, 10.dp)
                                .shadow(10.dp, CircleShape)
                        )
                    }
                },
                onClick = { GameMode.globalGameMode = GameModes.Menu },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = mainPurple,
                    contentColor = yellow
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(75.dp)
            )

            Button(
                content = {
                    Text(
                        text = "�",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = yellow,
                        style = TextStyle(
                            shadow = Shadow(
                                color = textShadowPurple,
                                offset = Offset(10.0f, 10.0f),
                                blurRadius = 3f,
                            )
                        ),
                        textAlign = TextAlign.Center
                    )
                },
                onClick = {
                    GameMode.globalGameMode = GameModes.Help
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = mainPurple,
                    contentColor = yellow
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(75.dp)
            )
        }


// addition of Mode and Difficulty Switch unique to Menu
        when (GameMode.globalGameMode) {

            GameModes.Menu -> {
                SeparatingLightLine()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(mainPurple)
                        .padding(bottom = 4.dp)
                ) {
                    Text(
                        text = "Mode",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 12.dp),
                        color = textBeige
                    )
                    Button(
                        onClick =
                            {
                                when (QuestionType.globalQuestionType) {
                                    QuestionTypes.Capitals -> {
                                        QuestionType.globalQuestionType =
                                            QuestionTypes.Domains
                                    }

                                    QuestionTypes.Domains -> {
                                        QuestionType.globalQuestionType =
                                            QuestionTypes.Capitals
                                    }
                                }
                            },
                        content = {
                            Text(
                                text = QuestionType.globalQuestionType.toString(),
                                fontSize = 18.sp
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = mainPurple,
                            contentColor = yellow
                        ),
                        border = BorderStroke(width = 2.dp, color = lightPurple),
                        modifier = Modifier
                            .shadow(25.dp, RoundedCornerShape(12.dp))
                    )

                    // difficulty switcher
                    Text(
                        text = "Difficulty",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(12.dp),
                        color = textBeige
                    )

                    Button(
                        onClick =
                            {
                                when (Difficulty.globalDifficulty) {
                                    DifficultyModes.Easy -> {
                                        Difficulty.globalDifficulty = DifficultyModes.Hard
                                    }

                                    DifficultyModes.Hard -> {
                                        Difficulty.globalDifficulty = DifficultyModes.Easy
                                    }
                                }
                            },
                        content = {
                            Text(
                                text = Difficulty.globalDifficulty.toString(),
                                fontSize = 18.sp
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = mainPurple,
                            contentColor = yellow
                        ),
                        border = BorderStroke(width = 2.dp, color = lightPurple),
                        modifier = Modifier
                            .shadow(25.dp, RoundedCornerShape(12.dp))
                    )
                }
            }

            // only Menu has a specific bar for changing difficulty and mode
            else -> {}
        }


// END OF NAVBAR --------------------------------------------------------


        // content Column
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(16.dp)
                )
                // fills available space
                .weight(1f)
        ) {


            // construct content
            when (GameMode.globalGameMode) {
                GameModes.Menu -> {
                    MenuLayoutContent()
                }

                GameModes.FourAnswers -> {
                    FourAnswersLayout()
                }

                GameModes.Typing -> {
                    TypingLayout()
                }

                GameModes.Help -> {
                    HelpLayout()
                }
            }
        }


        // trying to construct objects on the bottom
        when (GameMode.globalGameMode) {
            GameModes.Menu -> {
                MenuLayoutBottomSwitch()
                BottomCreditBar()
            }

            GameModes.FourAnswers -> {
                BottomCreditBar()
            }

            GameModes.Typing -> {
                BottomCreditBar()
            }

            GameModes.Help -> {
                BottomCreditBar()
            }
        }
    }
}

@Composable
fun SeparatingLightLine(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(1.dp)
            .background(color = lightPurple)
            .shadow(12.dp)
    ) {}
}

@Composable
fun MenuLayoutContent() {

    GameModeButtons(
        gameMode = GameModes.FourAnswers,
        imgSrc = painterResource(R.drawable.four_answers_logo),
        description = "Four Answers Layout Logo"
    )

    GameModeButtons(
        gameMode = GameModes.Typing,
        imgSrc = painterResource(R.drawable.typing_logo),
        description = "Typing Layout Logo"
    )
}

@Composable
fun GameModeButtons(gameMode: GameModes, imgSrc: Painter, description: String){
    Button(
        onClick = { GameMode.globalGameMode = gameMode },
        content = {
            Image(
                painter = imgSrc,
                contentDescription = description,
            )
        },
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = gameModeButtonColor
        ),
        modifier = Modifier
            .size(220.dp)
    )
}

// only in MenuLayout - Switch for reversing questions
@Composable
fun MenuLayoutBottomSwitch(){
    Column (
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(mainPurple)
                .padding(top = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.switch_hint),
                fontSize = 24.sp,
                color = textBeige
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(mainPurple)
                .height(48.dp)
        ) {
            var internalReverseAnswerAndQuestion by remember {
                mutableStateOf(
                    reverseAnswerAndQuestion
                )
            }

            Switch(
                checked = internalReverseAnswerAndQuestion,
                onCheckedChange = {
                    internalReverseAnswerAndQuestion = it
                    reverseAnswerAndQuestion = it
                },
                thumbContent = { Box(modifier = Modifier.size(10.dp)) },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = yellow,
                    uncheckedTrackColor = yellow,
                    checkedThumbColor = lightPurple,
                    uncheckedThumbColor = lightPurple,
                    checkedBorderColor = Color.Unspecified,
                    uncheckedBorderColor = Color.Unspecified
                ),
                modifier = Modifier.padding(start = 24.dp, end = 24.dp)
            )
        }
    }
}

@Composable
fun BottomCreditBar(){
    when (GameMode.globalGameMode){
        GameModes.Menu -> SeparatingLightLine()
        else -> {}
    }
    Row {
        Text(
            text = stringResource(R.string.credit),
            color = greyText,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(mainPurple)
                .padding(bottom = 52.dp)
        )
    }
}

@Composable
fun QuestionBar(text:String, modifier:Modifier = Modifier){
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 0.dp)
            .background(barYellow, RoundedCornerShape(12.dp))
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = blackText,
            modifier = Modifier
                .padding(24.dp)
        )
    }
}

@Composable
fun StreakBox(streakNum:Int, stillScoring:Boolean){
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ){
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(barYellow, RoundedCornerShape(12.dp))
                .size(92.dp)
                .padding(bottom = 12.dp)
                .fillMaxHeight()
        ){
            Text(
                text =
                    if(stillScoring){
                        streakNum.toString()
                    } else {
                        "0"
                    },
                color = blackText,
                fontSize = 48.sp
            )
            Text(
                text = "Streak",
                color = blackText
            )
        }
    }
}

@Composable
fun FourAnswersLayout(){

    val internalCoroutineScope = rememberCoroutineScope()

    var nextQuestion by remember { mutableIntStateOf(0) }
    var answerFontSize by remember { mutableStateOf(32.sp) }

    var correctAnswer by remember { mutableStateOf("") }
    val answerOptions = remember { mutableListOf("","","","") }

    var streak by remember { mutableIntStateOf(0) }

    var scoring by remember { mutableStateOf(true) }

    var index by remember { mutableIntStateOf(0) }

    val defaultButtonColor = lightPurple

    val buttonColor = remember { mutableStateListOf(lightPurple,lightPurple,lightPurple,lightPurple) }


    fun checkAnswer(buttonNumber: Int){
        if (answerOptions[buttonNumber] == correctAnswer) {
            internalCoroutineScope.launch {
                buttonColor[buttonNumber] = greenCorrect
                delay(500)
                buttonColor[buttonNumber] = defaultButtonColor

                if(!scoring){
                    scoring = true
                    nextQuestion++

                } else {
                    streak++
                    nextQuestion++
                }
            }

        } else {
            internalCoroutineScope.launch {
                buttonColor[buttonNumber] = Color(0xFFDC4C4C)
                delay(300)
                buttonColor[buttonNumber] = defaultButtonColor
            }
            streak = 0
            scoring = false
        }
    }



    // to get FontSize based on answers
    answerFontSize = assignQuestionsAndAnswers()

    LaunchedEffect(Unit,nextQuestion) {

        if (questionList.isNotEmpty() && answerList.isNotEmpty() && answerList.size == questionList.size) {

            when(Difficulty.globalDifficulty) {

                DifficultyModes.Easy -> {
                    answerOptions.clear()

                    val previous = correctAnswer

                    index = (0..questionList.size - 1).random()
                    correctAnswer = answerList [index]

                    while (correctAnswer == previous) {
                        index = (0..answerList.size - 1).random()
                        correctAnswer = answerList[index]
                    }

                    answerOptions.add(correctAnswer)

                    while (answerOptions.size < 4) {
                        val toBeAdded = answerList[(0..answerList.size - 1).random()]
                        if (!answerOptions.contains(toBeAdded)) {
                            answerOptions.add(toBeAdded)
                        }
                    }

                    val x = shuffleList(answerOptions.toMutableList())
                    answerOptions[0] = x[0]
                    answerOptions[1] = x[1]
                    answerOptions[2] = x[2]
                    answerOptions[3] = x[3]
                }

                DifficultyModes.Hard -> {
                    // answerOptions doesn't need to be cleared, because the state and length of list will break, and because the values get overwritten anyway

                    val previous = correctAnswer

                    index = (0..answerList.size - 1).random()
                    correctAnswer = answerList[index]

                    while (correctAnswer == previous) {
                        index = (0..answerList.size - 1).random()
                        correctAnswer = answerList[index]
                    }


                    var firstLetter = correctAnswer[0]
                    var goingDown = true
                    var goingUp = true
                    var currentOccurrence:Int

                    if (index-1 > -1){
                        currentOccurrence = index - 1
                    } else {
                        currentOccurrence = index + 1
                        goingDown = false
                    }

                    var firstOccurrence = index
                    var lastOccurrence = index
                    val thisAnswerOptions:MutableSet<String> = mutableSetOf()

                    var indexToStart = 0

                    if(firstLetter == '.'){
                        firstLetter = correctAnswer[1]
                        indexToStart = 1
                    }

                    while(goingDown){
                        // compare firstLetter of correctAnswer with firstOccurrence
                        if (answerList[currentOccurrence][indexToStart] == firstLetter){
                            firstOccurrence = currentOccurrence
                            // count down
                            if(currentOccurrence-1 > -1){
                                currentOccurrence -= 1
                                // done
                            } else {
                                goingDown = false
                            }
                        } else {
                            // no occurrence found further down -> found firstOccurrence above
                            goingDown = false
                        }
                    }

                    // reset of currentOccurrence
                    currentOccurrence = if (index + 1 < answerList.lastIndex){
                        index + 1
                    } else {
                        index
                    }

                    while(goingUp){
                        if (answerList[currentOccurrence][indexToStart] == firstLetter){
                            lastOccurrence = currentOccurrence
                            // count down
                            if(currentOccurrence+1 < answerList.lastIndex){
                                currentOccurrence += 1
                                // done
                            } else {
                                goingUp = false
                            }
                        } else {
                            // no occurrence found further up -> found lastOccurrence below
                            goingUp = false
                        }
                    }

                    thisAnswerOptions.add(correctAnswer)

                    when (lastOccurrence-firstOccurrence){
                        // none found || not enough -> 3 found would be enough
                        in 0..2 -> {
                            for(l in 0..lastOccurrence-firstOccurrence){
                                if(thisAnswerOptions.size<4){
                                    thisAnswerOptions.add(answerList[firstOccurrence+l])
                                }
                            }
                            while(thisAnswerOptions.size < 4) {
                                val toBeAdded = answerList[(0..answerList.lastIndex).random()]
                                if(!thisAnswerOptions.contains(toBeAdded)){
                                    thisAnswerOptions.add(toBeAdded)
                                }
                            }
                        }

                        else -> {
                            while(thisAnswerOptions.size < 4) {
                                val toBeAdded = answerList[(firstOccurrence..lastOccurrence).random()]
                                if(!thisAnswerOptions.contains(toBeAdded)){
                                    thisAnswerOptions.add(toBeAdded)
                                }
                            }
                        }
                    }

                    // has to happen one by one else the new state is not recognized
                    val x = shuffleList(thisAnswerOptions.toMutableList())
                    answerOptions[0] = x[0]
                    answerOptions[1] = x[1]
                    answerOptions[2] = x[2]
                    answerOptions[3] = x[3]
                }
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {

        // 4x4 Layout
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            QuestionBar(text = questionList[index], modifier = Modifier.padding(bottom = 32.dp))

            Row {
                FourAnswersLayoutButtons(buttonNumber = 0, answerOption = answerOptions[0], fontSize = answerFontSize, color = buttonColor[0], onClick = { checkAnswer(0) })

                Spacer(modifier = Modifier.size(4.dp))

                FourAnswersLayoutButtons(buttonNumber = 1, answerOption = answerOptions[1], fontSize = answerFontSize, color = buttonColor[1], onClick = { checkAnswer(1) })

            }

            Spacer(modifier = Modifier.size(4.dp))

            Row {
                FourAnswersLayoutButtons(buttonNumber = 2, answerOption = answerOptions[2], fontSize = answerFontSize, color = buttonColor[2], onClick = { checkAnswer(2) })


                Spacer(modifier = Modifier.size(4.dp))

                FourAnswersLayoutButtons(buttonNumber = 3, answerOption = answerOptions[3], fontSize = answerFontSize, color = buttonColor[3], onClick = { checkAnswer(3) })
            }
        }

        Spacer(modifier = Modifier.size(80.dp))
        StreakBox(streakNum = streak, stillScoring = scoring)
    }
}

@Composable
fun FourAnswersLayoutButtons(buttonNumber: Int, answerOption:String, fontSize: TextUnit, color:Color, onClick: (buttonNumber:Int) -> Unit){

    Button(
        content = {
            Text(
                text = answerOption,
                textAlign = TextAlign.Center,
                fontSize = fontSize,
                color = blackText
            )
        },
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(12.dp),
        onClick = {
            onClick(buttonNumber)
        },
        modifier = Modifier
            .size(120.dp)

    )
}

@Composable
fun TypingLayout() {

    val internalCoroutineScope = rememberCoroutineScope()

    var nextQuestion by remember { mutableIntStateOf(0) }
    var correctAnswer by remember { mutableStateOf("") }
    var index by remember { mutableIntStateOf(0) }

    var scoring by remember { mutableStateOf(true) }
    var streak by remember { mutableIntStateOf(0) }

    val focusRequester = FocusRequester()
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    var buttonText by remember { mutableStateOf("hint") }
    var boxColor by remember { mutableStateOf(Color.Unspecified) }
    var guess by remember {
        mutableStateOf(
            TextFieldValue(
                text =
                    when(QuestionType.globalQuestionType){
                        QuestionTypes.Capitals -> { "" }

                        QuestionTypes.Domains -> {
                            if(reverseAnswerAndQuestion) {
                                ""
                            } else {
                                "."
                            }
                        }
                    },
                selection = TextRange(1)
            )
        )
    }

    // return value is not used because there exists no answer options to assign a fontSize to
    assignQuestionsAndAnswers()


    LaunchedEffect(nextQuestion) {
        if (questionList.isNotEmpty() && answerList.isNotEmpty() && questionList.size == answerList.size) {

            val previous = correctAnswer
            index = (0..answerList.size - 1).random()
            correctAnswer = answerList[index]

            while (correctAnswer == previous) {
                index = (0..answerList.size - 1).random()
                correctAnswer = answerList[index]
            }
        }
    }

    LaunchedEffect(guess.text) {
        // if correct
        if (toStandardCase(guess.text) == toStandardCase(correctAnswer)) {
            internalCoroutineScope.launch {
                boxColor = greenCorrect
                delay(600)
                boxColor = Color.Unspecified
                guess = TextFieldValue(
                    text =
                        when(QuestionType.globalQuestionType){
                            QuestionTypes.Capitals -> { "" }

                            QuestionTypes.Domains -> {
                                if(reverseAnswerAndQuestion) {
                                    ""
                                } else {
                                    "."
                                }
                            }
                        },
                    selection = TextRange(1)
                )
            }
            buttonText = "hint"
            if(!scoring){
                scoring = true
                nextQuestion++
            } else {
                streak++
                nextQuestion++
            }
        }
    }


// 2 columns allow there to be space for the keyboard
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {

        // content specific to TypingLayout
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            QuestionBar(text = questionList[index])


            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(boxColor, shape = RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .size(120.dp)
                    .padding(12.dp)
            ) {
                TextField(
                    value = guess,
                    onValueChange = { guess = it },
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .weight(2F)
                        .focusRequester(focusRequester)
                )

                Button(
                    content = {
                        Text(
                            text = buttonText,
                            color = blackText
                        )
                    },
                    onClick = {
                        when (buttonText) {
                            "hint" -> {
                                if (QuestionType.globalQuestionType == QuestionTypes.Domains && !reverseAnswerAndQuestion) {

                                    val hintText = "." + correctAnswer[1]
                                    guess = TextFieldValue(
                                        text = hintText,
                                        selection = TextRange(hintText.length)
                                    )
                                    buttonText = "solve"

                                } else {
                                    val hintText = "" + correctAnswer[0]
                                    guess = TextFieldValue(
                                        text = hintText,
                                        selection = TextRange(hintText.length)
                                    )
                                    buttonText = "hint 2"
                                }

                            }

                            "hint 2" -> {
                                val hintText = "" + correctAnswer[0] + correctAnswer[1]
                                guess = TextFieldValue(
                                    text = hintText,
                                    selection = TextRange(hintText.length)
                                )
                                buttonText = "last hint"
                            }

                            "last hint" -> {
                                val hintText =
                                    "" + correctAnswer[0] + correctAnswer[1] + correctAnswer[2]
                                guess = TextFieldValue(
                                    text = hintText,
                                    selection = TextRange(hintText.length)
                                )
                                buttonText = "solve"
                            }

                            "solve" -> {
                                scoring = false
                                guess = TextFieldValue(
                                    text = correctAnswer,
                                    selection = TextRange(correctAnswer.length)
                                )
                                internalCoroutineScope.launch {
                                    delay(1250)
                                }
                                buttonText = "hint"
                                streak = 0
                                scoring = false
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = lightPurple),
                    modifier = Modifier
                        .weight(1F)
                        .height(60.dp)
                )
            }

            Spacer(modifier = Modifier.size(12.dp))
            StreakBox(streakNum = streak, stillScoring = scoring)
        }
    }
}

@Composable
fun HelpLayout() {

            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "This Game Has 2 Game Modes:",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        style = TextStyle(
                            textDecoration = TextDecoration.Underline
                        )
                    )
                }

                Spacer(modifier = Modifier.size(12.dp))
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "4 Answers:",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.size(8.dp))
                Row {
                    Text(text = "select the button with the correct answer for your streak to get higher")
                }

                Spacer(modifier = Modifier.size(16.dp))
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "Typing:",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.size(8.dp))
                Row {
                    Text(text = "type the right answer, if you have no idea you can use the hint button")
                }

                Spacer(modifier = Modifier.size(20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "This Game Has 2 Types Of Questions:",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        style = TextStyle(
                            textDecoration = TextDecoration.Underline
                        )
                    )
                }

                Spacer(modifier = Modifier.size(12.dp))
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "Domains:",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.size(8.dp))
                Row {
                    Text(text = "combine domains and countries/regions")
                }

                Spacer(modifier = Modifier.size(16.dp))
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "Capitals:",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.size(8.dp))
                Row {
                    Text(text = "combine capitals and countries/regions")
                }

                Spacer(modifier = Modifier.size(8.dp))
                Row {
                    Text(text = "switch between modes by pressing the buttons with the yellow text in the main menu")
                }

                Spacer(modifier = Modifier.size(32.dp))
                Row {
                    Text(text = "you can reverse what the questions and what the answers are with a switch in the main menu")
                }
            }
}


// assisting functions
fun toStandardCase(t:String): String{
    var newString = ""
    var h:Int
    var toGo = t.length

    for (i in t){
        h = i.code
        toGo--

        when (h){
            in 65..90 -> {
                h += 32
                newString += h.toChar()
            }
            in 97..122 -> {
                newString += h.toChar()
            }
            32 -> {
                if(toGo == 0 || toGo == t.length-1){
                    //don't add
                } else {
                    newString += h.toChar()
                }
            }
            // Å of Åland, á of Bogotá
            143, 160 -> {
                newString += 'a'
            }
            // ç of Curaçao
            135 -> {
                newString += 'c'
            }
            // é of Réunion
            130 -> {
                newString += 'e'
            }
        }
    }
    return newString
}

fun assignQuestionsAndAnswers() : TextUnit {
    when(QuestionType.globalQuestionType){

        QuestionTypes.Capitals -> {

            when(reverseAnswerAndQuestion){
                true -> {
                    questionList = unsortedCapitalCountriesD
                    answerList = sortedCapitalsD
                    return  16.sp
                }
                // default
                false -> {
                    questionList = unsortedCapitalsC
                    answerList = sortedCapitalCountriesC
                    return  16.sp
                }
            }
        }

        QuestionTypes.Domains -> {

            when(reverseAnswerAndQuestion){
                true -> {
                    questionList = unsortedDomainsA
                    answerList = sortedShortCountriesA
                    return  16.sp
                }
                // default, question = country, answer = domain
                false -> {
                    questionList = unsortedDomainCountriesB
                    answerList = sortedDomainsB
                    return 32.sp
                }
            }

        }
    }
}

fun shuffleList(myList:MutableList<String>):MutableList<String>{
    val newList = mutableListOf("","","","")
    val list = myList
    var r:Int

    for(i in list.lastIndex downTo 0){
        r = (0..list.lastIndex).random()
        newList[i] = list[r]
        list.removeAt(r)
    }
    return newList
}