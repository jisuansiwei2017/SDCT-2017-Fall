import time
def Factorize(n):
	if(n==1):
		return 1
	i=2
	ans=[]
	while i <= n:
		if(n==1):
			break
		if n % i ==0:
			print i
			n=n/i
			ans.append(i)
		else :
			i+=1
	return ans

time_start=time.time()
print Factorize(6249564825603)
time_end=time.time()
print 1000*(time_end-time_start)