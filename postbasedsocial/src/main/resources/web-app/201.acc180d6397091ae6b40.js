"use strict";(self.webpackChunkpbs_proto=self.webpackChunkpbs_proto||[]).push([[201],{6201:(T,l,r)=>{r.r(l),r.d(l,{UserModule:()=>C});var i=r(8583),c=r(2305),e=r(7716),m=r(8613),a=r(2808),f=r(5552);function d(n,o){if(1&n&&(e.TgZ(0,"div",1),e.TgZ(1,"div",2),e.TgZ(2,"div",3),e._UZ(3,"img",4),e.qZA(),e.TgZ(4,"div",5),e.TgZ(5,"h1"),e._uU(6),e.qZA(),e.TgZ(7,"p"),e._uU(8),e.qZA(),e.TgZ(9,"p"),e._uU(10),e.qZA(),e.TgZ(11,"p"),e._uU(12),e.ALo(13,"date"),e.qZA(),e.qZA(),e.qZA(),e.TgZ(14,"div"),e._UZ(15,"app-sub-buttons",6,7),e.qZA(),e.qZA()),2&n){const t=e.oxw();e.xp6(3),e.Q6J("src",null==t.user?null:t.user.picture,e.LSH),e.xp6(3),e.Oqu(null==t.user?null:t.user.username),e.xp6(2),e.hij("id: ",null==t.user?null:t.user.id,""),e.xp6(2),e.hij("Role: ",null==t.user?null:t.user.role,""),e.xp6(2),e.hij("Registered: ",e.xi3(13,6,null==t.user?null:t.user.registrationTime,"mediumDate"),""),e.xp6(3),e.Q6J("author",t.user)}}let h=(()=>{class n{constructor(){this.user=null}ngOnInit(){}}return n.\u0275fac=function(t){return new(t||n)},n.\u0275cmp=e.Xpm({type:n,selectors:[["app-user-info"]],inputs:{user:"user"},decls:1,vars:1,consts:[["class","bg-white",4,"ngIf"],[1,"bg-white"],[1,"div-flex","flex-wrap"],[1,"div-profile-image"],[1,"size-box-max-200px",3,"src"],[1,"div-profile-info"],[3,"author"],["subButtons",""]],template:function(t,s){1&t&&e.YNc(0,d,17,9,"div",0),2&t&&e.Q6J("ngIf",s.user)},directives:[i.O5,f.L],pipes:[i.uU],styles:[""]}),n})();var v=r(6334),p=r(1095);function g(n,o){if(1&n){const t=e.EpF();e.TgZ(0,"button",5),e.NdJ("click",function(){return e.CHM(t),e.oxw().fetchMoreOnClick()}),e._uU(1,"Fetch more"),e.qZA()}}function U(n,o){if(1&n){const t=e.EpF();e.TgZ(0,"button",5),e.NdJ("click",function(){return e.CHM(t),e.oxw().fetchMoreOnClick()}),e._uU(1,"Refresh"),e.qZA()}}const Z=[{path:"",children:[{path:"",component:(()=>{class n{constructor(t,s,u){this.userService=t,this.activatedRoute=s,this.recentService=u,this.userInfo=null,this.posts=[]}ngOnInit(){console.log("UserComponent onInit"),this.activatedRoute.params.subscribe(t=>{const s=t.parameter;this.userService.getUserInfo(s).subscribe(u=>{this.userInfo=u}),this.recentService.setUserId(s),this.recentService.removeAll(),this.posts=this.recentService.getPosts(),this.recentService.tryFetchMore()})}fetchMoreOnClick(){this.recentService.tryFetchMore()}}return n.\u0275fac=function(t){return new(t||n)(e.Y36(m.K),e.Y36(c.gz),e.Y36(a.X))},n.\u0275cmp=e.Xpm({type:n,selectors:[["app-user"]],decls:6,vars:4,consts:[[3,"user"],[3,"posts"],[1,"div-flex"],[1,"margin-centre-horiz"],["mat-button","","class","bg-blue-1",3,"click",4,"ngIf"],["mat-button","",1,"bg-blue-1",3,"click"]],template:function(t,s){1&t&&(e._UZ(0,"app-user-info",0),e._UZ(1,"app-feed",1),e.TgZ(2,"div",2),e.TgZ(3,"div",3),e.YNc(4,g,2,0,"button",4),e.YNc(5,U,2,0,"button",4),e.qZA(),e.qZA()),2&t&&(e.Q6J("user",s.userInfo),e.xp6(1),e.Q6J("posts",s.posts),e.xp6(3),e.Q6J("ngIf",s.posts.length>0),e.xp6(1),e.Q6J("ngIf",0===s.posts.length))},directives:[h,v.e,i.O5,p.lW],styles:[""]}),n})()}]}];let b=(()=>{class n{}return n.\u0275fac=function(t){return new(t||n)},n.\u0275mod=e.oAB({type:n}),n.\u0275inj=e.cJS({imports:[[c.Bz.forChild(Z)],c.Bz]}),n})();var x=r(3430);let C=(()=>{class n{}return n.\u0275fac=function(t){return new(t||n)},n.\u0275mod=e.oAB({type:n}),n.\u0275inj=e.cJS({imports:[[i.ez,b,x.m,p.ot]]}),n})()}}]);