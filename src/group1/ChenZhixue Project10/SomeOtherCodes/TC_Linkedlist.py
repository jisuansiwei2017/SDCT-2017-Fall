class Node:
	def __init__(self,objt=None,prev=None,nex=None):
		self.object=objt
		self.prev=prev
		self.nex=nex
	def appd(self,nextnode):
		self.nex=nextnode
		nextnode.prev=self
	def delt(self):
		if(self.nex==None):
			self.prev.nex=None
		else :
			self.prev.nex=self.nex
			self.nex.prev=self.prev
	def __str__(self): 
		return "%s"%(self.object.object)

def newnode():
	return Node()

#A rubbish queue that I implement using singled list
class Queue:
	def __init__(self,ob=None):
		self.s=Node(ob)
		self.e=self.s
	def push(self,ob):
		if self.s==None or self.s.object==None:
			self.s=Node(ob)
			self.e=self.s
		else :
			self.e.nex=Node(ob)
			self.e=self.e.nex
	def get(self):
		return self.s.object
	def pop(self):
		if not self.s.object == None:
			if not self.s.nex == None:
				self.s=self.s.nex
			else : 
				self.s.object=None
	def printf(self):
		t=self.s
		while not t.object==None:
			print t.object
			if not t.nex==None:
				t=t.nex
			else :
				break
	def empty(self):
		if self.s==None:
			return 1
		if self.s.object==None:
			return 1
		return 0
'''L=[]
for i in range(0,20):
	L.append(i)
q=Queue(L[0])
for i in range(1,20):
	q.push(L[i])
L[0]=5
print q.get()
print q.empty()'''




