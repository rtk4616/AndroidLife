/* //device/java/android/android/app/IServiceConnection.aidl
**
** Copyright 2007, The Android Open Source Project
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
** You may obtain a copy of the License at
**
**     http://www.apache.org/licenses/LICENSE-2.0
**
** Unless required by applicable law or agreed to in writing, software
** distributed under the License is distributed on an "AS IS" BASIS,
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
** See the License for the specific language governing permissions and
** limitations under the License.
*/

package android.app;

import android.content.ComponentName;

/**
 * Refer from VirtualAPK
 *
 * @author CaMnter
 */

oneway interface IServiceConnection {

    void connected(in ComponentName name, IBinder service);

    /** Added in Android O */
    //void connected(in ComponentName name, IBinder service, boolean dead);

}