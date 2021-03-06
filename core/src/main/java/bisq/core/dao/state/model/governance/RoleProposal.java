/*
 * This file is part of Bisq.
 *
 * Bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package bisq.core.dao.state.model.governance;

import bisq.core.dao.governance.param.Param;
import bisq.core.dao.governance.proposal.ProposalType;
import bisq.core.dao.state.model.ImmutableDaoStateModel;
import bisq.core.dao.state.model.blockchain.TxType;

import bisq.common.app.Version;

import io.bisq.generated.protobuffer.PB;

import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.concurrent.Immutable;

@Immutable
@Slf4j
@EqualsAndHashCode(callSuper = true)
@Value
public final class RoleProposal extends Proposal implements ImmutableDaoStateModel {
    private final Role role;

    public RoleProposal(Role role) {
        this(role.getName(),
                role.getLink(),
                role,
                Version.PROPOSAL,
                new Date().getTime(),
                "");
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // PROTO BUFFER
    ///////////////////////////////////////////////////////////////////////////////////////////

    private RoleProposal(String name,
                         String link,
                         Role role,
                         byte version,
                         long creationDate,
                         String txId) {
        super(name,
                link,
                version,
                creationDate,
                txId);

        this.role = role;
    }

    @Override
    public PB.Proposal.Builder getProposalBuilder() {
        final PB.RoleProposal.Builder builder = PB.RoleProposal.newBuilder()
                .setRole(role.toProtoMessage());
        return super.getProposalBuilder().setRoleProposal(builder);
    }

    public static RoleProposal fromProto(PB.Proposal proto) {
        final PB.RoleProposal proposalProto = proto.getRoleProposal();
        return new RoleProposal(proto.getName(),
                proto.getLink(),
                Role.fromProto(proposalProto.getRole()),
                (byte) proto.getVersion(),
                proto.getCreationDate(),
                proto.getTxId());
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    ///////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ProposalType getType() {
        return ProposalType.BONDED_ROLE;
    }

    @Override
    public Param getQuorumParam() {
        return Param.QUORUM_ROLE;
    }

    @Override
    public Param getThresholdParam() {
        return Param.THRESHOLD_ROLE;
    }

    @Override
    public TxType getTxType() {
        return TxType.PROPOSAL;
    }

    @Override
    public Proposal cloneProposalAndAddTxId(String txId) {
        return new RoleProposal(getName(),
                getLink(),
                this.getRole(),
                getVersion(),
                getCreationDate().getTime(),
                txId);
    }

    @Override
    public String toString() {
        return "RoleProposal{" +
                "\n     role=" + role +
                "\n} " + super.toString();
    }
}
