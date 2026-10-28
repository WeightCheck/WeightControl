package com.huhtamaki.weightcontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                WeightScreen()
            }
        }
    }
}

@Composable
fun WeightScreen() {

    var wet by remember { mutableStateOf("214.4") }
    var dry by remember { mutableStateOf("73.4") }
    var boneDry by remember { mutableStateOf("71") }
    var targetBoneDry by remember { mutableStateOf("72.5") }
    var dryerTemp by remember { mutableStateOf("224") }
    var speed by remember { mutableStateOf("35") }

    val wetValue = wet.replace(",", ".").toDoubleOrNull()
    val dryValue = dry.replace(",", ".").toDoubleOrNull()
    val boneDryValue = boneDry.replace(",", ".").toDoubleOrNull()
    val targetValue = targetBoneDry.replace(",", ".").toDoubleOrNull()

    var machineDryness: Double? = null
    var moistureContent: Double? = null
    var wetShouldBe: Double? = null
    var deviation: Double? = null
    var adjustment: Double? = null

    if (
        wetValue != null &&
        dryValue != null &&
        boneDryValue != null &&
        targetValue != null &&
        wetValue > 0 &&
        dryValue > 0
    ) {

        machineDryness =
            (boneDryValue / wetValue) * 100

        moistureContent =
            ((dryValue - boneDryValue) / dryValue) * 100

        wetShouldBe =
            (targetValue / machineDryness) * 100

        deviation =
            boneDryValue - targetValue

        adjustment =
            wetShouldBe - wetValue
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF003DA5)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {

                Text(
                    "HUHTAMAKI",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Weight Control",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (machineDryness != null) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF2F2F7)
                )
            ) {

                Column {

                    Text(
                        text = "PRODUCT PARAMETERS",
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF003DA5))
                            .padding(8.dp),
                        fontWeight = FontWeight.Bold
                    )

                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {

                        ResultRow(
                            "Machine dryness",
                            "%.2f %%".format(machineDryness)
                        )

                        ResultRow(
                            "Moisture content",
                            "%.2f %%".format(moistureContent)
                        )

                        Divider()

                        ResultRow(
                            "Wet weight",
                            "%.1f g".format(wetValue)
                        )

                        ResultRow(
                            "Wet should be",
                            "%.1f g".format(wetShouldBe)
                        )

                        ResultRow(
                            "Adjustment",
                            "%.1f g".format(adjustment)
                        )

                        Divider()

                        ResultRow(
                            "Dry weight",
                            "%.1f g".format(dryValue)
                        )

                        ResultRow(
                            "Bone dry",
                            "%.1f g".format(boneDryValue)
                        )

                        ResultRow(
                            "Target bone dry",
                            "%.1f g".format(targetValue)
                        )

                        Divider()

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                Arrangement.SpaceBetween
                        ) {

                            Text(
                                "Deviation",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )

                            Text(
                                "%.1f g".format(deviation ?: 0.0),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color =
                                    if ((deviation ?: 0.0) < 0)
                                        Color.Red
                                    else
                                        Color(0xFF008000)
                            )
                        }

                        Divider()

                        ResultRow(
                            "Dryer temp",
                            "$dryerTemp °C"
                        )

                        ResultRow(
                            "Speed",
                            "$speed KO"
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(10.dp)
            ) {

                Text(
                    "INPUT DATA",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    CompactField(
                        "Wet Weight",
                        wet,
                        { wet = it },
                        Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    CompactField(
                        "Dry Weight",
                        dry,
                        { dry = it },
                        Modifier.weight(1f)
                    )
                }

                Row {
                    CompactField(
                        "Bone Dry",
                        boneDry,
                        { boneDry = it },
                        Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    CompactField(
                        "Target BD",
                        targetBoneDry,
                        { targetBoneDry = it },
                        Modifier.weight(1f)
                    )
                }

                Row {
                    CompactField(
                        "Dryer °C",
                        dryerTemp,
                        { dryerTemp = it },
                        Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    CompactField(
                        "Speed",
                        speed,
                        { speed = it },
                        Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun CompactField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        modifier = modifier
    )
}

@Composable
fun ResultRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text = label,
            fontSize = 14.sp
        )

        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}