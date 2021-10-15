package com.github.mhelmi.unsplashphotos.ui.photos.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.github.mhelmi.unsplashphotos.common.state.UiState
import com.github.mhelmi.unsplashphotos.domain.photos.model.Photo
import com.github.mhelmi.unsplashphotos.domain.photos.model.PhotosConst
import com.github.mhelmi.unsplashphotos.domain.photos.usecase.GetPhotosUseCase
import com.github.mhelmi.unsplashphotos.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
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

  @Mock
  lateinit var photosStateObserver: Observer<UiState<List<Any>>>

  @Captor
  lateinit var photosStateCaptor: ArgumentCaptor<UiState<List<Any>>>


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
  fun setUp() {
    photosViewModel = PhotosViewModel(getPhotosUseCase, testDispatcher)
  }

  @After
  fun tearDown() {
    photosViewModel.photoListState.removeObserver(photosStateObserver)
    testDispatcher.cleanupTestCoroutines()
  }

  @Test
  fun `when loadPhotos() and getPhotosUseCase() return not empty list then return success ui state`() =
    runBlockingTest {

      Mockito.`when`(getPhotosUseCase(PhotosConst.START_PAGE, PhotosConst.PAGE_SIZE))
        .thenReturn(flowOf(successResponse))

      photosViewModel.photoListState.observeForever(photosStateObserver)
      photosViewModel.loadPhotos()

      Mockito.verify(photosStateObserver, Mockito.times(3))
        .onChanged(photosStateCaptor.capture())

      val values = photosStateCaptor.allValues
      /* For testing only emit Error at first because loadPhotos called in init before preparing our test */
      assertThat(values[0], instanceOf(UiState.Error::class.java))
      assertThat(values[1], instanceOf(UiState.Loading::class.java))
      assertThat(values[2], instanceOf(UiState.Success::class.java))
      Assert.assertEquals(successResponse, (values[2] as UiState.Success).data)
    }

  @Test
  fun `when loadPhotos() and getPhotosUseCase() return 5 items or more then return success state with extra Ad after every 5 photos`() =
    runBlockingTest {
      Mockito.`when`(getPhotosUseCase(PhotosConst.START_PAGE, PhotosConst.PAGE_SIZE))
        .thenReturn(flowOf(successWithAdsResponse))

      photosViewModel.photoListState.observeForever(photosStateObserver)
      photosViewModel.loadPhotos()

      Mockito.verify(photosStateObserver, Mockito.times(3))
        .onChanged(photosStateCaptor.capture())

      val values = photosStateCaptor.allValues
      /* For testing only emit Error at first because loadPhotos called in init before preparing our test */
      assertThat(values[0], instanceOf(UiState.Error::class.java))
      assertThat(values[1], instanceOf(UiState.Loading::class.java))
      assertThat(values[2], instanceOf(UiState.Success::class.java))
      Assert.assertEquals(
        successWithAdsResponse.count() + 1,
        (values[2] as UiState.Success).data.count()
      )
    }

  @Test
  fun `when loadPhotos() and getPhotosUseCase() return empty list then return Empty state`() =
    runBlockingTest {
      Mockito.`when`(getPhotosUseCase(PhotosConst.START_PAGE, PhotosConst.PAGE_SIZE))
        .thenReturn(flowOf(emptyResponse))

      photosViewModel.photoListState.observeForever(photosStateObserver)
      photosViewModel.loadPhotos()

      Mockito.verify(photosStateObserver, Mockito.times(3))
        .onChanged(photosStateCaptor.capture())

      val values = photosStateCaptor.allValues
      /* For testing only emit Error at first because loadPhotos called in init before preparing our test */
      assertThat(values[0], instanceOf(UiState.Error::class.java))
      assertThat(values[1], instanceOf(UiState.Loading::class.java))
      assertThat(values[2], instanceOf(UiState.Empty::class.java))
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