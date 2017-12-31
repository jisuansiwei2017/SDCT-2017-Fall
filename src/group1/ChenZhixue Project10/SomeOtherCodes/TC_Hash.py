from TC_Linkedlist import *
class HashObj:
	def __init__(self,ob,key):
		self.object=ob
		self.key=key

def CreateSlots(L,n):
	for i in range(0,n):
		L.append(newnode())
def HashF(L):
	a=L[0]*10000+L[1]*1000+L[2]*100+L[3]*10+L[4]+L[5]*1000+L[6]*100+L[7]*10+L[8]
	return a
def Hash(object,L): #Input HashObj
	h=HashF(object.key)
	T=Node(object)
	T.nex=L[h]
	L[h].prev=T
	L[h]=T
	return T
def OutputHash(L):
	for i in range(0,len(L)):
		#print i,
		t=L[i]
		if not t.object==None:
			print "\n"
		while not t.object==None:
			print t,
			t=t.nex
	print "END"
		#print "\n"
def TurntoHash(L1,L2):
	for i in range(0,len(L1)):
		L1[i]=HashObj(L1[i],L2[i])
def OutputNodes(L):
	for item in L:
		print item.prev
def Search(ob,t,L):
	a=L[t]
	if a.object == None:
		return 0
	if a.object.object==ob:
		return 1
	while not a.object.object==ob:
		if a.nex==None:
			return 0
		if a.nex.object==None:
			return 0
		else :
			a=a.nex
	return 1
'''L=[]
L1=[5,28,19,15,20,33,12,17,10]
L2=[5,28,19,15,20,33,12,17,10]
TurntoHash(L1,L2)
CreateSlots(L,9)
for i in range(0,len(L1)):
	L1[i]=Hash(L1[i],L)
print Search(7,1,L)
OutputHash(L)
OutputNodes(L1)'''


