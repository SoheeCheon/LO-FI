import React from 'react'
import box from '../assets/img/icon/box.png'
import calendar from '../assets/img/icon/calendar.png'
import pin from '../assets/img/icon/pin.png'
import animal from '../assets/img/icon/animal.png'
import person from '../assets/img/icon/user.png'
import phone from '../assets/img/icon/phone-call.png'
import './DetailPage.css'
import BackTopNab from '../components/BackTopNab'

export default function DetailPage() {
  return (
    <div className='detail-container'>
      <BackTopNab back={-1} submit={""}/>
      <img className="detail-img" src="data:image/jpeg;base64,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" alt="" />
      <div className='detail-info'>
        <div className='detail-span'>
          <img src={pin} alt="" width={20} height={20}/>
          <span>대전 유성구 궁동</span>
        </div>
        <div className='detail-span' >
          <img src={calendar} alt="" width={20} height={20} />
          <span>2022.08.31</span>
        </div>
        <div className='detail-span'>
          <img src={box} alt=""  width={20} height={20}/>
          <span>캉골 에코백</span>
        </div>
        <div className='detail-span'>
          <img src={person} alt="" width={20} height={20} />
          <span>대전 유성 경찰서</span>
        </div>
        <div className='detail-span'>
          <img src={phone} alt="" width={20} height={20} />
          <span>042-123-4567</span>
        </div>
      </div>
      <div className='detail-button'>
        <button>습득물 신고</button>
      </div>
    </div>
  )
}
