import React from 'react';
import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
// 컴포넌트
import FooterBar from './components/FooterBar';
import MainPage from './pages/MainPage';
import SearchPage from './pages/SearchPage';
import RegisterPage from './pages/RegisterPage';
import LoginPage from './pages/LoginPage';
import GoogleLogin from './pages/GoogleLogin';

import DetailPage from './pages/DetailPage';
import AddPage from './pages/AddPage';
import AddDetailPage from './pages/AddDetailPage';
import ProfilePage from './pages/ProfilePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import KakaoLogin from './pages/KakaoLogin';
// reducer
import * as getData from './reducres/data';
import  {bindActionCreators} from 'redux';
import { connect } from 'react-redux'


function App() {

  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path='/' element={<MainPage />}/>
          <Route path='/login' element={<LoginPage />}/>
          <Route path='/register' element={<RegisterPage />}/>
          <Route path='/search' element={<SearchPage />}/>
          <Route path='/search/:id' element={<DetailPage />}/>
          <Route path='/add' element={<AddPage />}/>
          <Route path='/add/:category' element={<AddDetailPage/>}/>
          <Route path='/profile/' element={<ProfilePage/>}/>
          <Route path='/oauth2/:token' element={<GoogleLogin/>}/>
        </Routes>
        <FooterBar/>
      </Router>
    </div>
  );
}

export default connect(
  (state) => ({
      data: state.data.data
      loading: state.data.pending,
      error: state.data.error
  }),
  (dispatch) => ({
      CounterActions: bindActionCreators(getData, dispatch),
  })
)(App);