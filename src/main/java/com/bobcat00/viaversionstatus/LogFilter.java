// ViaVersionStatus - Logs players' client versions
// Copyright 2022 Bobcat00
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package com.bobcat00.viaversionstatus;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

public class LogFilter implements Filter
{
    private String filterStr;
    
    public LogFilter(String filterStr)
    {
        this.filterStr = filterStr;
    }
    
    public Filter.Result checkMessage(String message)
    {
        if (message.contains(filterStr))
        {
            return Filter.Result.DENY;
        }
        else
        {
            return Filter.Result.NEUTRAL;
        }
    }
    
    public LifeCycle.State getState()
    {
        try
        {
            return LifeCycle.State.STARTED;
        }
        catch (Exception localException)
        {
        }
        return null;
    }
    
    public void initialize()
    {
    }
    
    public boolean isStarted()
    {
        return true;
    }
    
    public boolean isStopped()
    {
        return false;
    }
    
    public void start()
    {
    }
    
    public void stop()
    {
    }
    
    public Filter.Result filter(LogEvent event)
    {
        return checkMessage(event.getMessage().getFormattedMessage());
    }
    
    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object... arg4)
    {
        return checkMessage(message);
    }
    
    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object arg4)
    {
        return checkMessage(message);
    }
    
    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, Object message, Throwable arg4)
    {
        return checkMessage(message.toString());
    }
    
    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, Message message, Throwable arg4)
    {
        return checkMessage(message.getFormattedMessage());
    }
    
    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5)
    {
        return checkMessage(message);
    }
    
    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5,
            Object arg6)
    {
        return checkMessage(message);
    }
    
    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5,
            Object arg6, Object arg7)
    {
        return checkMessage(message);
    }
    
    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5,
            Object arg6, Object arg7, Object arg8)
    {
        return checkMessage(message);
    }
    
    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5,
            Object arg6, Object arg7, Object arg8, Object arg9)
    {
        return checkMessage(message);
    }
    
    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5,
            Object arg6, Object arg7, Object arg8, Object arg9, Object arg10)
    {
        return checkMessage(message);
    }
    
    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5,
            Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11)
    {
        return checkMessage(message);
    }
    
    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5,
            Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12)
    {
        return checkMessage(message);
    }
    
    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5,
            Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13)
    {
        return checkMessage(message);
    }
    
    public Filter.Result getOnMatch()
    {
        return Filter.Result.NEUTRAL;
    }
    
    public Filter.Result getOnMismatch()
    {
        return Filter.Result.NEUTRAL;
    }

}
