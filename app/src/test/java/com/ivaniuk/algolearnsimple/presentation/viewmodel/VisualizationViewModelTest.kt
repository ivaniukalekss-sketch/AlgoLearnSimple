package com.ivaniuk.algolearnsimple.presentation.viewmodel

import com.ivaniuk.algolearnsimple.domain.model.Speed
import com.ivaniuk.algolearnsimple.domain.model.VisualizationStep
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmVisualizer
import com.ivaniuk.algolearnsimple.utils.MainDispatcherRule
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class VisualizationViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var visualizer: AlgorithmVisualizer

    private lateinit var viewModel: VisualizationViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        `when`(visualizer.algorithmName).thenReturn("Test Algorithm")
        `when`(visualizer.getDefaultInput()).thenReturn(listOf(1, 2, 3))
        `when`(visualizer.visualize(any())).thenReturn(flowOf(emptyList()))

        viewModel = VisualizationViewModel(visualizer)
    }

    @Test
    fun testInitialState() = runTest {
        assertEquals(0, viewModel.currentStepIndex.value)
        assertFalse(viewModel.isPlaying.value)
        assertEquals(Speed.MEDIUM, viewModel.speed.value)
    }

    @Test
    fun testNextStep() = runTest {
        val mockSteps = listOf(
            VisualizationStep(0, description = "Step 1"),
            VisualizationStep(1, description = "Step 2")
        )
        `when`(visualizer.visualize(any())).thenReturn(flowOf(mockSteps))

        viewModel = VisualizationViewModel(visualizer)
        advanceUntilIdle()

        viewModel.nextStep()
        assertEquals(1, viewModel.currentStepIndex.value)
    }

    @Test
    fun testPreviousStep() = runTest {
        val mockSteps = listOf(
            VisualizationStep(0, description = "Step 1"),
            VisualizationStep(1, description = "Step 2")
        )
        `when`(visualizer.visualize(any())).thenReturn(flowOf(mockSteps))

        viewModel = VisualizationViewModel(visualizer)
        advanceUntilIdle()

        viewModel.nextStep()
        viewModel.previousStep()
        assertEquals(0, viewModel.currentStepIndex.value)
    }

    @Test
    fun testSetSpeed() = runTest {
        viewModel.setSpeed(Speed.FAST)
        assertEquals(Speed.FAST, viewModel.speed.value)
    }

    @Test
    fun testResetToFirstStep() = runTest {
        val mockSteps = listOf(
            VisualizationStep(0, description = "Step 1"),
            VisualizationStep(1, description = "Step 2")
        )
        `when`(visualizer.visualize(any())).thenReturn(flowOf(mockSteps))

        viewModel = VisualizationViewModel(visualizer)
        advanceUntilIdle()

        viewModel.nextStep()
        viewModel.resetToFirstStep()
        assertEquals(0, viewModel.currentStepIndex.value)
    }

    @Test
    fun testGoToLastStep() = runTest {
        val mockSteps = listOf(
            VisualizationStep(0, description = "Step 1"),
            VisualizationStep(1, description = "Step 2"),
            VisualizationStep(2, description = "Step 3")
        )
        `when`(visualizer.visualize(any())).thenReturn(flowOf(mockSteps))

        viewModel = VisualizationViewModel(visualizer)
        advanceUntilIdle()

        viewModel.goToLastStep()
        assertEquals(2, viewModel.currentStepIndex.value)
    }
}