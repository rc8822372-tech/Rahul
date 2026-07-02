package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MahadevLogo(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    animate: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "logo_anim")
    
    // Smooth glow pulsing animation
    val pulseScale by if (animate) {
        infiniteTransition.animateFloat(
            initialValue = 0.95f,
            targetValue = 1.05f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse"
        )
    } else {
        rememberUpdatedState(1f)
    }

    val rotationAngle by if (animate) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(20000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "rotate"
        )
    } else {
        rememberUpdatedState(0f)
    }

    // Color definitions - updated for the "Sleek Interface" theme
    val saffronDark = Color(0xFF004786) // Sleek deep luxury blue
    val saffronLight = Color(0xFFA8C7FF) // Sleek light accent
    val goldDark = Color(0xFF38495F) // Medium slate frame / orbit
    val goldLight = Color(0xFFD1E4FF) // Bright ice blue highlight glow
    val deepCharcoal = Color(0xFF111318) // Sleek dark slate background

    Box(
        modifier = modifier
            .size(size * pulseScale)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(deepCharcoal.copy(alpha = 0.95f), deepCharcoal),
                    center = Offset.Unspecified,
                    radius = Float.POSITIVE_INFINITY
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Decorative rotating outer gold travel orbit circle
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            drawCircle(
                brush = Brush.linearGradient(
                    colors = listOf(goldDark, goldLight, saffronDark)
                ),
                style = Stroke(
                    width = 3.dp.toPx(),
                    pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                        intervals = floatArrayOf(20f, 15f),
                        phase = rotationAngle * 1.5f
                    )
                )
            )
        }

        // Inner Canvas drawing the Trident (Trishul) and taxi travel curves
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            val w = size.toPx()
            val h = size.toPx()
            val centerX = size.toPx() / 2f
            val centerY = size.toPx() / 2f

            // 1. Draw smooth travel trajectory sweep (Abstract road curve representation)
            val roadPath = Path().apply {
                moveTo(centerX - w * 0.35f, centerY + h * 0.25f)
                quadraticTo(
                    centerX, centerY + h * 0.35f,
                    centerX + w * 0.35f, centerY + h * 0.25f
                )
            }
            drawPath(
                path = roadPath,
                brush = Brush.horizontalGradient(listOf(saffronLight, goldLight)),
                style = Stroke(width = 4.dp.toPx())
            )

            // 2. Draw the Trishul (Trident)
            val trishulBrush = Brush.verticalGradient(
                colors = listOf(goldLight, goldDark, saffronDark)
            )

            // Center pillar shaft
            val shaftWidth = 5.dp.toPx()
            val shaftTop = centerY - h * 0.28f
            val shaftBottom = centerY + h * 0.22f
            
            drawLine(
                brush = trishulBrush,
                start = Offset(centerX, shaftTop),
                end = Offset(centerX, shaftBottom),
                strokeWidth = shaftWidth
            )

            // Outer curved prongs of the Trishul
            val prongPath = Path().apply {
                // Left prong
                moveTo(centerX - w * 0.16f, centerY - h * 0.14f)
                cubicTo(
                    centerX - w * 0.16f, centerY,
                    centerX - shaftWidth / 2, centerY,
                    centerX, centerY
                )
                // Right prong
                cubicTo(
                    centerX + shaftWidth / 2, centerY,
                    centerX + w * 0.16f, centerY,
                    centerX + w * 0.16f, centerY - h * 0.14f
                )
            }
            drawPath(
                path = prongPath,
                brush = trishulBrush,
                style = Stroke(width = 6.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )

            // Left prong flame tip
            drawPath(
                path = Path().apply {
                    moveTo(centerX - w * 0.16f, centerY - h * 0.14f)
                    lineTo(centerX - w * 0.19f, centerY - h * 0.11f)
                    lineTo(centerX - w * 0.13f, centerY - h * 0.11f)
                    close()
                },
                brush = trishulBrush
            )

            // Right prong flame tip
            drawPath(
                path = Path().apply {
                    moveTo(centerX + w * 0.16f, centerY - h * 0.14f)
                    lineTo(centerX + w * 0.13f, centerY - h * 0.11f)
                    lineTo(centerX + w * 0.19f, centerY - h * 0.11f)
                    close()
                },
                brush = trishulBrush
            )

            // Middle main tip
            drawPath(
                path = Path().apply {
                    moveTo(centerX, centerY - h * 0.34f)
                    lineTo(centerX - w * 0.05f, centerY - h * 0.26f)
                    lineTo(centerX + w * 0.05f, centerY - h * 0.26f)
                    close()
                },
                brush = trishulBrush
            )

            // The Damru (Sacred Drum) at the center of the shaft
            val damruY = centerY + h * 0.04f
            val damruW = w * 0.12f
            val damruH = h * 0.07f
            val damruPath = Path().apply {
                moveTo(centerX - damruW, damruY - damruH)
                lineTo(centerX + damruW, damruY - damruH)
                lineTo(centerX, damruY)
                lineTo(centerX + damruW, damruY + damruH)
                lineTo(centerX - damruW, damruY + damruH)
                lineTo(centerX, damruY)
                close()
            }
            drawPath(path = damruPath, brush = trishulBrush)
        }
    }
}
