c    "Copyright 2009 Prof. Robert Dalrymple, Prof. M. Gomez Gesteira, Dr Benedict Rogers, 
c     Dr Alejandro Crespo, Dr Muthukumar Narayanaswamy, Dr Shan Zou, Dr Andrea Panizzo "
c
c    This file is part of SPHYSICS.
c
c    SPHYSICS is free software; you can redistribute it and/or modify
c    it under the terms of the GNU General Public License as published by
c    the Free Software Foundation; either version 3 of the License, or
c    (at your option) any later version.
c
c    SPHYSICS is distributed in the hope that it will be useful,
c    but WITHOUT ANY WARRANTY; without even the implied warranty of
c    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
c    GNU General Public License for more details.
c
c    You should have received a copy of the GNU General Public License
c    along with this program.  If not, see <http://www.gnu.org/licenses/>.

      subroutine ac   !ac_KC_2D.f
c
      include 'common.2D'

       call ac_main           
       
       do i=nbp1,np   
         
         sum_wab(i) = sum_wab(i) + adh*pm(i)/rhop(i)  !self contribution       
         if(sum_wab(i).lt.0.1) then
            one_over_sum_wab=1.
         else
           one_over_sum_wab=1./sum_wab(i)
         endif
       
         xcor(i) = eps*ux(i) * one_over_sum_wab
         zcor(i) = eps*wx(i) * one_over_sum_wab
       
       
       enddo
              
       return
       end

