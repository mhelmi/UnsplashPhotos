package com.github.mhelmi.unsplashphotos.ui.photos.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.mhelmi.unsplashphotos.common.state.UiState
import com.github.mhelmi.unsplashphotos.domain.photos.model.Photo
import com.github.mhelmi.unsplashphotos.domain.photos.model.PhotosConst
import com.github.mhelmi.unsplashphotos.domain.photos.usecase.GetPhotosUseCase
import com.github.mhelmi.unsplashphotos.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PhotosViewModelTest {

  @get:Rule
  var instantExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  var mainCoroutineRule = MainCoroutineRule()

  private val testDispatcher = TestCoroutineDispatcher()

  private lateinit var photosViewModel: PhotosViewModel

  @Mock
  lateinit var getPhotosUseCase: GetPhotosUseCase

  private val testResults: MutableList<UiState<List<Any>>> = mutableListOf()

  private val successResponse = listOf(Photo("1", "Picasso", "http://photourl.com/test.png"))
  private val successWithAdsResponse = listOf(
    Photo("1", "Picasso", "http://photourl.com/test1.png"),
    Photo("2", "Helmi", "http://photourl.com/test2.png"),
    Photo("3", "Ahmed", "http://photourl.com/test3.png"),
    Photo("4", "Yasser", "http://photourl.com/test4.png"),
    Photo("5", "Hamada", "http://photourl.com/test5.png")
  )
  private val emptyResponse = emptyList<Photo>()

  @Before
  fun beforeEachTest() {
    photosViewModel = PhotosViewModel(getPhotosUseCase, testDispatcher)
  }

  @After
  fun afterEachTest() {
    testResults.clear()
    testDispatcher.cleanupTestCoroutines()
  }

  @Test
  fun `when loadPhotos() and getPhotosUseCase() return not empty list then return success ui state`() =
    runBlockingTest {
      Mockito.`when`(getPhotosUseCase(PhotosConst.START_PAGE, PhotosConst.PAGE_SIZE))
        .thenReturn(flowOf(successResponse))

      val job = launch {
        photosViewModel.photoListState.toList(testResults)
      }
      photosViewModel.loadPhotos()
      /* For testing only emit Error at first because loadPhotos called in init before preparing our test */
      assertThat(testResults[0], instanceOf(UiState.Error::class.java))
      assertThat(testResults[1], instanceOf(UiState.Loading::class.java))
      assertThat(testResults[2], instanceOf(UiState.Success::class.java))
      Assert.assertEquals(successResponse, (testResults[2] as UiState.Success).data)
      job.cancel()
    }

  @Test
  fun `when loadPhotos() and getPhotosUseCase() return 5 items or more then return success state with extra Ad after every 5 photos`() =
    runBlockingTest {
      Mockito.`when`(getPhotosUseCase(PhotosConst.START_PAGE, PhotosConst.PAGE_SIZE))
        .thenReturn(flowOf(successWithAdsResponse))

      val job = launch {
        photosViewModel.photoListState.toList(testResults)
      }
      photosViewModel.loadPhotos()

      /* For testing only emit Error at first because loadPhotos called in init before preparing our test */
      assertThat(testResults[0], instanceOf(UiState.Error::class.java))
      assertThat(testResults[1], instanceOf(UiState.Loading::class.java))
      assertThat(testResults[2], instanceOf(UiState.Success::class.java))
      Assert.assertEquals(
        successWithAdsResponse.count() + 1,
        (testResults[2] as UiState.Success).data.count()
      )
      job.cancel()
    }

  @Test
  fun `when loadPhotos() and getPhotosUseCase() return empty list then return Empty state`() =
    runBlockingTest {
      Mockito.`when`(getPhotosUseCase(PhotosConst.START_PAGE, PhotosConst.PAGE_SIZE))
        .thenReturn(flowOf(emptyResponse))

      val job = launch {
        photosViewModel.photoListState.toList(testResults)
      }
      photosViewModel.loadPhotos()

      /* For testing only emit Error at first because loadPhotos called in init before preparing our test */
      assertThat(testResults[0], instanceOf(UiState.Error::class.java))
      assertThat(testResults[1], instanceOf(UiState.Loading::class.java))
      assertThat(testResults[2], instanceOf(UiState.Empty::class.java))
      job.cancel()
    }

  @Test
  fun `when loadPhotos() and getPhotosUseCase() return empty list and it is not the start page then stop pagination`() =
    runBlockingTest {
      Mockito.`when`(getPhotosUseCase(3, PhotosConst.PAGE_SIZE))
        .thenReturn(flowOf(emptyResponse))

      photosViewModel.loadPhotos(3)

      assert(photosViewModel.isLastPage)
    }

  @Test
  fun `when loadPhotos() and getPhotosUseCase() return empty list and it is the start page then do not stop pagination`() =
    runBlockingTest {
      Mockito.`when`(getPhotosUseCase(PhotosConst.START_PAGE, PhotosConst.PAGE_SIZE))
        .thenReturn(flowOf(emptyResponse))

      photosViewModel.loadPhotos(PhotosConst.START_PAGE)

      assert(photosViewModel.isLastPage.not())
    }

}