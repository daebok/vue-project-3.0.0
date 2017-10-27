import React from 'react';
export default class GoodsindexList extends React.Component{
	render(){
		let data = this.props.data;
    	if(!data){return false;}
    	let avatarPhoto = $(".serverUrl").val();
		return (
	      <div>
	      	<div className="goodsList clearfix">
	      		<ul className="clearfix">
	                {
	                data.map(function(k,v){
	                  var urlval = "/scoreshop/goodsDetails.html?id="+k.id;
	                  let scoreval = (k.score).toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,');
	                  let avatarPhotoUrl = avatarPhoto+k.picSmall;
	                  let classType = k.lessNum ==0 ? "sellOut" : "";
	                  if(v==0){
	                  	return <li key={v} className={classType}><a href={urlval}><img src={avatarPhotoUrl}/></a><div className="describe"><p className="ptitle clearfix"><a href={urlval}>{k.goodsName}</a></p><p className="score"><font>{scoreval}</font>积分</p><p className="num">剩余数量：{k.lessNum}</p></div><div className="sellOutbg"></div></li>
	                  }
	                  else{
		                  if((v+1)%4==0){
		                  	return <li key={v} style={{"margin":"0px"}} className={classType}><a href={urlval}><img src={avatarPhotoUrl}/></a><div className="describe"><p className="ptitle clearfix"><a href={urlval}>{k.goodsName}</a></p><p className="score"><font>{scoreval}</font>积分</p><p className="num">剩余数量：{k.lessNum}</p></div><div className="sellOutbg"></div></li>
		                  }
		                  else{
		                  	return <li key={v} className={classType}><a href={urlval}><img src={avatarPhotoUrl}/></a><div className="describe"><p className="ptitle clearfix"><a href={urlval}>{k.goodsName}</a></p><p className="score"><font>{scoreval}</font>积分</p><p className="num">剩余数量：{k.lessNum}</p></div><div className="sellOutbg"></div></li>
		                  }
		              }
	                })
	              }
	            </ul>
	      	</div>	
	      </div>
	    );
	}
};