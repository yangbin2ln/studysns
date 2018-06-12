package com.eeesns.tshow.util;

import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.eeesns.tshow.dao.BaseDao;

@Component
public class UUUID {
	@Resource
	private BaseDao baseDao;

	/**
	 * 生成32位数字字符串
	 * 
	 * @return
	 */
	public static final String getNextIntValue() {
		int totalLength = 32;
		Random random = new Random();
		StringBuffer num = new StringBuffer();
		String time = String.valueOf(System.currentTimeMillis());
		int timeLength = time.length();
		int cycleLength = totalLength - timeLength;
		for (int i = 0; i < cycleLength; i++)
			num.append(random.nextInt(10));

		return (new StringBuilder(String.valueOf(time))).append(num.toString()).toString();
	}

	public String getInvitationId() {
		String sql = "select ci.invitation_id from create_invitation ci where ci.create_invitation_id =1";
		int invitationId = baseDao.findIntBySql(sql);
		String sql2 = "update create_invitation set invitation_id=invitation_id+1 where create_invitation_id=1";
		baseDao.executeUpdateBySql(sql2);
		String newinvitationId = createInvitationId(invitationId);
		return newinvitationId;

	}

	private String createInvitationId(Integer invitationId) {
		invitationId++;
		String code = invitationId.toString();
		int length = code.length();
		if (length < 5) {
			for (int i = 0; i < (5 - length); i++) {
				code = "0" + code;
			}
		}
		return code;

	}
}
