from TC_Hash import *
from TC_Linkedlist import *

def newl(l):
	l1=[]
	for i in range(0,len(l)):
		l1.append(l[i])
	return l1

L00=[2,6,4,1,3,7,0,5,8]
L0=[8,1,5,7,3,6,4,0,2]
ans=[]
kkey=0
def HashF(L):
	a=L[0]*10000+L[1]*1000+L[2]*100+L[3]*10+L[4]+L[5]*1000+L[6]*100+L[7]*10+L[8]
	return a
CreateSlots(ans,100000)
H=HashObj(newl(L00),newl(L00))
Hash(H,ans)
q=Queue(newl(L00))
top=newl(q.get())
q.pop()
i=0
def Findzero(L):
	for i in range(0,len(L)):
		if L[i]==0:
			return i
def Exchange(L,i,j):
	t=L[i];L[i]=L[j];L[j]=t
	return L
def Sumy(L):
	#OutputHash(ans);print "1"
	k=HashF(L)
	s=Search(L,k,ans)
	if not s:
		if L==L0:
			print "Done Here",
			print L
			return 1
		Hash(HashObj(newl(L),newl(L)),ans)
		q.push(newl(L))
	return 0
def dojob(L):
	a = Findzero(L);
	if a==0:
		Exchange(L,0,1)
		kkey=Sumy(L)
		Exchange(L,0,1)
		if kkey :
			return 1
		Exchange(L,0,3)
		kkey=Sumy(L)
		Exchange(L,0,3)
		if kkey :
			return 1
	if a==1:
		Exchange(L,1,0)
		kkey=Sumy(L)
		Exchange(L,1,0)
		if kkey :
			return 1
		Exchange(L,1,0)
		kkey=Sumy(L)
		Exchange(L,1,0)
		if kkey :
			return 1
		Exchange(L,1,4)
		kkey=Sumy(L)
		Exchange(L,1,4)
		if kkey :
			return 1
	if a==2:
		Exchange(L,2,1)
		kkey=Sumy(L)
		Exchange(L,2,1)
		if kkey :
			return 1
		Exchange(L,2,5)
		kkey=Sumy(L)
		Exchange(L,2,5)
		if kkey :
			return 1
	if a==3:
		Exchange(L,3,0)
		kkey=Sumy(L)
		Exchange(L,3,0)
		if kkey :
			return 1
		Exchange(L,3,4)
		kkey=Sumy(L)
		Exchange(L,3,4)
		if kkey :
			return 1
		Exchange(L,3,6)
		kkey=Sumy(L)
		Exchange(L,3,6)
		if kkey :
			return 1
	if a==4:
		Exchange(L,4,1)
		kkey=Sumy(L)
		Exchange(L,4,1)
		if kkey :
			return 1
		Exchange(L,4,3)
		kkey=Sumy(L)
		Exchange(L,4,3)
		if kkey :
			return 1
		Exchange(L,4,5)
		kkey=Sumy(L)
		Exchange(L,4,5)
		if kkey :
			return 1
		Exchange(L,4,7)
		kkey=Sumy(L)
		Exchange(L,4,7)
		if kkey :
			return 1
	if a==5:
		Exchange(L,5,2)
		kkey=Sumy(L)
		Exchange(L,5,2)
		if kkey :
			return 1
		Exchange(L,5,4)
		kkey=Sumy(L)
		Exchange(L,5,4)
		if kkey :
			return 1
		Exchange(L,5,8)
		kkey=Sumy(L)
		Exchange(L,5,8)
		if kkey :
			return 1
	if a==6:
		#OutputHash(ans);print "5"
		Exchange(L,6,3)
		#OutputHash(ans);print "2"
		kkey=Sumy(L)
		Exchange(L,6,3)
		if kkey :
			return 1
		Exchange(L,6,7)
		kkey=Sumy(L)
		Exchange(L,6,7)
		if kkey :
			return 1
	if a==7:
		Exchange(L,7,4)
		kkey=Sumy(L)
		Exchange(L,7,4)
		if kkey :
			return 1
		Exchange(L,7,6)
		kkey=Sumy(L)
		Exchange(L,7,6)
		if kkey :
			return 1
		Exchange(L,7,8)
		kkey=Sumy(L)
		Exchange(L,7,8)
		if kkey :
			return 1
	if a==8:
		Exchange(L,8,7)
		kkey=Sumy(L)
		Exchange(L,8,7)
		if kkey :
			return 1
		Exchange(L,7,5)
		kkey=Sumy(L)
		Exchange(L,7,5)
		if kkey :
			return 1
	return kkey

#OutputHash(ans);print "3"
while dojob(top)==0 :
	if q.empty():
		print "DEAD"
		break
	top=q.get()
	q.pop()
	#OutputHash(ans);print "4"
print "Done!"
'''L2=[2,6,4,0,3,7,1,5,8]
L3=[2,6,4,1,3,7,0,5,8]
L4=[0,6,4,2,3,0,1,5,8]
L5=[0,6,4,2,3,7,1,0,0]
#l2=HashObj(L2,L2)
#Hash(l2,ans)
l3=HashObj(L3,L3)
Hash(l3,ans)
L4=HashObj(L4,L4)
Hash(L4,ans)
L5=HashObj(L5,L5)
Hash(L5,ans)
print Search(L2,HashF(L2),ans)'''
